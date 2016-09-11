package com.unioncloud.newpay.data.repository.login.datasource;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.unioncloud.newpay.data.entity.ResultEntity;
import com.unioncloud.newpay.data.entity.login.LoginResultEntity;
import com.unioncloud.newpay.data.exception.RemoteDataException;
import com.unioncloud.newpay.data.net.NetRequest;
import com.unioncloud.newpay.data.net.NetResponse;
import com.unioncloud.newpay.data.net.webservice.WebServiceEngine;
import com.unioncloud.newpay.data.repository.WebServiceUrlConst;
import com.unioncloud.newpay.data.utils.JsonUtils;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * Created by cwj on 16/8/8.
 */
public class CloudLoginStore implements LoginStore {

    @Override
    public Observable<LoginResultEntity> login(final String userNo, final String password, final String mac) {
        return Observable.create(new Observable.OnSubscribe<NetResponse>() {
            @Override
            public void call(Subscriber<? super NetResponse> subscriber) {
                NetRequest request = new NetRequest(WebServiceUrlConst.URL,
                        WebServiceUrlConst.NAMESPACE, WebServiceUrlConst.LOGIN_ACTION);
                request.addParam("userNo", userNo);
                request.addParam("passwd", password);
                request.addParam("mac", mac);
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
        }).map(new Func1<NetResponse, LoginResultEntity>() {
            @Override
            public LoginResultEntity call(NetResponse netResponse) {
                String json = netResponse.getStringData();
                try {
                    ResultEntity<LoginResultEntity> entity = JsonUtils.fromJson(json,
                            new TypeToken<ResultEntity<LoginResultEntity>>(){}.getType());
                    if (entity.isSuccess()) {
                        return entity.getData();
                    } else {
                        throw Exceptions.propagate(new RemoteDataException(entity.getErrorMsg()));
                    }
                } catch (JsonSyntaxException e) {
                    throw Exceptions.propagate(new RemoteDataException("解析服务接口数据失败"));
                }
            }
        });
    }

    @Override
    public Observable<Boolean> changePassword(final String shopId, final String userId,
                                     final String newPassword, final String oldPassword) {
        return Observable.create(new Observable.OnSubscribe<NetResponse>() {
            @Override
            public void call(Subscriber<? super NetResponse> subscriber) {
                NetRequest request = new NetRequest(WebServiceUrlConst.URL,
                        WebServiceUrlConst.NAMESPACE, WebServiceUrlConst.CHANGE_PASSWORD);
                request.addParam("userId", userId);
                request.addParam("shopId", shopId);
                request.addParam("newPasswd", newPassword);
                request.addParam("oldPasswd", oldPassword);
                try {
                    NetResponse response = WebServiceEngine.getInstance().executeRequest(request);
                    if (response.getResponseCode() == 200) {
                        subscriber.onNext(response);
                    } else {
                        subscriber.onError(new RemoteDataException("访问接口异常"));
                    }
                } catch (IOException e) {
                    subscriber.onError(new RemoteDataException("无法连接服务器"));
                }
            }
        }).map(new Func1<NetResponse, Boolean>() {
            @Override
            public Boolean call(NetResponse netResponse) {
                String json = netResponse.getStringData();
                try {
                    ResultEntity<Object> entity = JsonUtils.fromJson(json,
                            new TypeToken<ResultEntity<Object>>(){}.getType());
                    if (entity.isSuccess()) {
                        return true;
                    } else {
                        throw Exceptions.propagate(new RemoteDataException(entity.getErrorMsg()));
                    }
                } catch (JsonSyntaxException e) {
                    throw Exceptions.propagate(new RemoteDataException("解析服务接口数据失败"));
                }
            }
        });
    }
}
