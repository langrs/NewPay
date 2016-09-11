package com.unioncloud.newpay.data.repository.pos.datasource;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.unioncloud.newpay.data.entity.ResultEntity;
import com.unioncloud.newpay.data.entity.pos.RegisterPosResultEntity;
import com.unioncloud.newpay.data.exception.RemoteDataException;
import com.unioncloud.newpay.data.net.NetRequest;
import com.unioncloud.newpay.data.net.NetResponse;
import com.unioncloud.newpay.data.net.webservice.WebServiceEngine;
import com.unioncloud.newpay.data.repository.WebServiceUrlConst;
import com.unioncloud.newpay.data.utils.JsonUtils;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.pos.PosRegister;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * Created by cwj on 16/8/12.
 */
public class CloudPosStore implements PosStore {

    private static CloudPosStore instance;

    public static CloudPosStore getInstance() {
        if (instance == null) {
            instance = new CloudPosStore();
        }
        return instance;
    }

    @Override
    public Observable<Boolean> register(final PosRegister info) {
        return Observable.create(new Observable.OnSubscribe<NetResponse>() {
            @Override
            public void call(Subscriber<? super NetResponse> subscriber) {
                NetRequest request = new NetRequest(WebServiceUrlConst.URL,
                        WebServiceUrlConst.NAMESPACE, WebServiceUrlConst.POS_REGISTER_ACTION);
                request.addParam("shopNo", info.getShopNo());
                request.addParam("storeNo", info.getStoreNo());
                request.addParam("cashierNo", info.getCashierNo());
                request.addParam("mac", info.getMac());
                request.addParam("posNo", info.getPosNo());
//                request.addParam("posType", info.getPosType());
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
                    ResultEntity entity = JsonUtils.fromJson(json,
                            new TypeToken<ResultEntity<RegisterPosResultEntity>>(){}.getType());
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

    @Override
    public Observable<Boolean> save(PosInfo pos) {
        return null;
    }

    @Override
    public Observable<PosInfo> getCached() {
        return null;
    }
}
