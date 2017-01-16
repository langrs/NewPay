package com.unioncloud.newpay.data.repository.vip.datasource;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.unioncloud.newpay.data.entity.ResultEntity;
import com.unioncloud.newpay.data.entity.vip.QueryVipEntity;
import com.unioncloud.newpay.data.entity.vip.VipEntity;
import com.unioncloud.newpay.data.exception.RemoteDataException;
import com.unioncloud.newpay.data.net.NetRequest;
import com.unioncloud.newpay.data.net.NetResponse;
import com.unioncloud.newpay.data.net.webservice.WebServiceEngine;
import com.unioncloud.newpay.data.repository.WebServiceUrlConst;
import com.unioncloud.newpay.data.utils.JsonUtils;
import com.unioncloud.newpay.domain.model.pos.PosInfo;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * Created by cwj on 16/8/17.
 */
public class VipDataStore implements VipStore {

    @Override
    public Observable<VipEntity> queryByCd(String cdInfo, PosInfo posInfo) {
        QueryVipEntity entity = new QueryVipEntity();
        entity.setCdInfo(cdInfo);
//        return queryVip(entity);
        return queryVipNew(entity);
    }

    @Override
    public Observable<VipEntity> queryByBillNo(String billNo, PosInfo posInfo) {
        QueryVipEntity entity = new QueryVipEntity();
        entity.setBillno(billNo);
//        return queryVip(entity);
        return queryVipNew(entity);
    }

    private Observable<VipEntity> queryVip(final QueryVipEntity entity) {

        return Observable.create(new Observable.OnSubscribe<NetResponse>() {
            @Override
            public void call(Subscriber<? super NetResponse> subscriber) {
                NetRequest request = new NetRequest(WebServiceUrlConst.getUrl(),
                        WebServiceUrlConst.NAMESPACE, WebServiceUrlConst.QUERY_VIP);
                request.addParam("cdInfo", entity.getCdInfo());
                request.addParam("billno", entity.getBillno());
                request.addParam("shopId", entity.getShopId());
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
        }).map(new Func1<NetResponse, VipEntity>() {
            @Override
            public VipEntity call(NetResponse netResponse) {
                String json = netResponse.getStringData();
                try {
                    ResultEntity<VipEntity> entity = JsonUtils.fromJson(json,
                            new TypeToken<ResultEntity<VipEntity>>(){}.getType());
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

    private Observable<VipEntity> queryVipNew(final QueryVipEntity entity) {
        Observable<VipEntity> wechat = Observable.create(new Observable.OnSubscribe<VipEntity>() {
            @Override
            public void call(Subscriber<? super VipEntity> subscriber) {
                if (isWechatVip(entity.getBillno())) {
                    subscriber.onNext(createWechatVip(entity.getBillno()));
                } else {
                    subscriber.onCompleted();
                }
            }
        });

        Observable<VipEntity> remote = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(queryVipJson(entity));
            }
        }).map(new Func1<String, VipEntity>() {
            @Override
            public VipEntity call(String json) {
                return parseVipJson(json);
            }
        });
        return Observable.concat(wechat, remote)
                .first();
    }

    private boolean isWechatVip(String vipCode) {
        return vipCode != null && vipCode.startsWith("48000");
    }

    private VipEntity createWechatVip(String vipCode) {
        VipEntity result = new VipEntity();
        result.setCkcode(vipCode);
        result.setCardtype("01");
        return result;
    }

    private String queryVipJson(QueryVipEntity entity) {
        NetRequest request = new NetRequest(WebServiceUrlConst.getUrl(),
                WebServiceUrlConst.NAMESPACE, WebServiceUrlConst.QUERY_VIP);
        request.addParam("cdInfo", entity.getCdInfo());
        request.addParam("billno", entity.getBillno());
        request.addParam("shopId", entity.getShopId());
        try {
            NetResponse response = WebServiceEngine.getInstance().executeRequest(request);
            if (response.getResponseCode() == 200) {
                return response.getStringData();
            } else {
                throw Exceptions.propagate(new RemoteDataException("访问接口异常"));
            }
        } catch (IOException e) {
            throw Exceptions.propagate(new RemoteDataException("无法连接服务器"));
        }
    }

    private VipEntity parseVipJson(String json) {
        try {
            ResultEntity<VipEntity> entity = JsonUtils.fromJson(json,
                    new TypeToken<ResultEntity<VipEntity>>(){}.getType());
            if (entity.isSuccess()) {
                return entity.getData();
            } else {
                throw Exceptions.propagate(new RemoteDataException(entity.getErrorMsg()));
            }
        } catch (JsonSyntaxException e) {
            throw Exceptions.propagate(new RemoteDataException("解析服务接口数据失败"));
        }
    }

}
