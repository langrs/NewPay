package com.unioncloud.newpay.data.repository.right.datasource;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.unioncloud.newpay.data.entity.ResultEntity;
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
 * Created by cwj on 16/9/13.
 */
public class CloudRightStore implements RightStore {

    @Override
    public Observable<Boolean> queryRefundRight(final String userNo, final String password) {
        return Observable.create(new Observable.OnSubscribe<NetResponse>() {
            @Override
            public void call(Subscriber<? super NetResponse> subscriber) {
                NetRequest request = new NetRequest(WebServiceUrlConst.URL,
                        WebServiceUrlConst.NAMESPACE, WebServiceUrlConst.QUERY_REFUND_RIGHT);
                request.addParam("userNo", userNo);
                request.addParam("passwd", password);
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
        }).map(new Func1<NetResponse, Boolean>() {
            @Override
            public Boolean call(NetResponse netResponse) {
                String json = netResponse.getStringData();
                try {
                    ResultEntity<String> entity = JsonUtils.fromJson(json,
                            new TypeToken<ResultEntity<String>>(){}.getType());
                    if (entity.isSuccess()) {
                        return "1".equals(entity.getData());
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
