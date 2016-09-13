package com.unioncloud.newpay.data.repository.notice.datasource;

import android.content.Context;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.unioncloud.newpay.data.db.DatabaseHelper;
import com.unioncloud.newpay.data.entity.ResultEntity;
import com.unioncloud.newpay.data.entity.notice.NoticeEntity;
import com.unioncloud.newpay.data.exception.RemoteDataException;
import com.unioncloud.newpay.data.net.NetRequest;
import com.unioncloud.newpay.data.net.NetResponse;
import com.unioncloud.newpay.data.net.webservice.WebServiceEngine;
import com.unioncloud.newpay.data.repository.WebServiceUrlConst;
import com.unioncloud.newpay.data.utils.JsonUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * Created by cwj on 16/9/8.
 */
public class CloudNoticeStore implements NoticeStore {
    DatabaseHelper helper;

    public CloudNoticeStore(Context context) {
        helper = DatabaseHelper.getInstance(context);
    }

    @Override
    public Observable<List<NoticeEntity>> queryAllNotice(String shopId) {
        return Observable.create(new Observable.OnSubscribe<List<NoticeEntity>>() {
            @Override
            public void call(Subscriber<? super List<NoticeEntity>> subscriber) {
                try {
                    Dao<NoticeEntity, String> noticeDao =  helper.getDao(NoticeEntity.class);
                    subscriber.onNext(noticeDao.queryForAll());
                    subscriber.onCompleted();
                } catch (SQLException e) {
                    subscriber.onError(new RemoteDataException("查询失败"));
                }
            }
        });
    }

    @Override
    public Observable<List<NoticeEntity>> queryNewNotice(String shopId) {
        return Observable.create(new Observable.OnSubscribe<NetResponse>() {
            @Override
            public void call(Subscriber<? super NetResponse> subscriber) {
                NetRequest request = new NetRequest(WebServiceUrlConst.URL,
                        WebServiceUrlConst.NAMESPACE, WebServiceUrlConst.QUERY_NOTICE);
                try {
                    NetResponse response = WebServiceEngine.getInstance().executeRequest(request);
                    if (response.getResponseCode() == 200) {
                        subscriber.onNext(response);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new RemoteDataException("访问接口异常"));
                    }
                } catch (IOException e) {
                    subscriber.onError(new RemoteDataException("无法连接服务器"));
                }
            }
        }).map(new Func1<NetResponse, List<NoticeEntity>>() {
            @Override
            public List<NoticeEntity> call(NetResponse netResponse) {
                String json = netResponse.getStringData();
                try {
                    ResultEntity<List<NoticeEntity>> entity = JsonUtils.fromJson(json,
                            new TypeToken<ResultEntity<List<NoticeEntity>>>(){}.getType());
                    if (entity.isSuccess()) {
                        return entity.getData();
                    } else {
                        throw Exceptions.propagate(new RemoteDataException(entity.getErrorMsg()));
                    }
                } catch (JsonSyntaxException e) {
                    throw Exceptions.propagate(new RemoteDataException("解析服务接口数据失败"));
                }
            }
        }).flatMap(new Func1<List<NoticeEntity>, Observable<NoticeEntity>>() {
            @Override
            public Observable<NoticeEntity> call(List<NoticeEntity> entityList) {
                return Observable.from(entityList);
            }
        }).filter(new Func1<NoticeEntity, Boolean>() {
            @Override
            public Boolean call(NoticeEntity entity) {
                return saveIfNotCached(entity);

            }
        }).toList();
    }

    private boolean saveIfNotCached(NoticeEntity entity) {
        try {
            Dao<NoticeEntity, String> noticeDao =  helper.getDao(NoticeEntity.class);
            NoticeEntity existEntity = noticeDao.queryForSameId(entity);
            if (existEntity == null) {
                return noticeDao.create(entity) == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
