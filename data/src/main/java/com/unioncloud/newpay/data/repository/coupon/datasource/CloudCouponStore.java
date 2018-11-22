package com.unioncloud.newpay.data.repository.coupon.datasource;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.unioncloud.newpay.data.entity.ResultEntity;
import com.unioncloud.newpay.data.entity.coupon.CouponEntity;
import com.unioncloud.newpay.data.entity.coupon.VipPointRebateEntity;
import com.unioncloud.newpay.data.exception.RemoteDataException;
import com.unioncloud.newpay.data.net.NetRequest;
import com.unioncloud.newpay.data.net.NetResponse;
import com.unioncloud.newpay.data.net.webservice.WebServiceEngine;
import com.unioncloud.newpay.data.repository.WebServiceUrlConst;
import com.unioncloud.newpay.data.repository.coupon.datasource.wechat.WeChatCouponConst;
import com.unioncloud.newpay.data.repository.coupon.datasource.wechat.WeChatCouponResponse;
import com.unioncloud.newpay.data.repository.coupon.datasource.wechat.WeChatCouponService;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.HttpRequest;
import com.unioncloud.newpay.data.utils.JsonUtils;
import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.domain.model.erp.QueryCardCommand;
import com.unioncloud.newpay.domain.model.erp.VipPointsRebate;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * Created by cwj on 16/9/6.
 */
public class CloudCouponStore implements CouponStore {
    @Override
    public Observable<Coupon> queryWeChatCoupon(final String code,final String shopId) {
        return Observable.create(new Observable.OnSubscribe<WeChatCouponResponse>(){
            @Override
            public void call(Subscriber<? super WeChatCouponResponse> subscriber) {
                String accessToken = null;
                NetRequest request = new NetRequest(WebServiceUrlConst.getUrl(),
                        WebServiceUrlConst.NAMESPACE, WebServiceUrlConst.QUERY_ACCESS_TOKEN);
                request.addParam("shopId", shopId);
                try {
                    NetResponse response = WebServiceEngine.getInstance().executeRequest(request);
                    if (response.getResponseCode() == 200) {
//                        分解accessToken
                        String json = response.getStringData();
                        try {
                            ResultEntity<String> entity = JsonUtils.fromJson(json,
                                    new TypeToken<ResultEntity<String>>(){}.getType());
                            if (entity.isSuccess()) {
                                accessToken = entity.getData();
                            } else {
                                throw Exceptions.propagate(new RemoteDataException(entity.getErrorMsg()));
                            }
                        } catch (JsonSyntaxException e) {
                            throw Exceptions.propagate(new RemoteDataException("解析服务接口数据失败"));
                        }
//                        分解accessToken完毕
//                        核销券
                        WeChatCouponService weChatCouponService = new WeChatCouponService();
                        WeChatCouponResponse weChatCouponResponse = weChatCouponService.dealWeChatCouponCode(code,accessToken);
                        if(weChatCouponResponse.getErrcode() != 0){
                            subscriber.onError(new RemoteDataException("券异常:"+weChatCouponResponse.getErrmsg()));
                        }else{
                            subscriber.onNext(weChatCouponResponse);
                        }
                    } else {
                        subscriber.onError(new RemoteDataException("访问接口异常"));
                    }
                } catch (IOException e) {
                    subscriber.onError(new RemoteDataException("无法连接服务器"));
                }
            }
        }).map(new Func1<WeChatCouponResponse, Coupon>() {
            @Override
            public Coupon call(WeChatCouponResponse weChatCouponResponse) {
                Coupon coupon = new Coupon();
                coupon.setCouponNo(code);
//                coupon.setCouponValue("50");
                coupon.setCouponValue(String.valueOf(weChatCouponResponse.getReduce_cost()/100));
                coupon.setFlag("3");
                coupon.setState("1");
                return coupon;
            }
        });
    }

    @Override
    public Observable<CouponEntity> queryCoupon(final String shopId, final String couponNo) {
        return Observable.create(new Observable.OnSubscribe<NetResponse>() {
            @Override
            public void call(Subscriber<? super NetResponse> subscriber) {
                NetRequest request = new NetRequest(WebServiceUrlConst.getUrl(),
                        WebServiceUrlConst.NAMESPACE, WebServiceUrlConst.QUERY_COUPON);
                request.addParam("shopId", shopId);
                request.addParam("giftCouponNo", couponNo);
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
        }).map(new Func1<NetResponse, CouponEntity>() {
            @Override
            public CouponEntity call(NetResponse netResponse) {
                String json = netResponse.getStringData();
                try {
                    ResultEntity<CouponEntity> entity = JsonUtils.fromJson(json,
                            new TypeToken<ResultEntity<CouponEntity>>(){}.getType());
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
    public Observable<CouponEntity> createSaleCoupon(final String orderId, final String userNo) {
        return Observable.create(new Observable.OnSubscribe<NetResponse>() {
            @Override
            public void call(Subscriber<? super NetResponse> subscriber) {
                NetRequest request = new NetRequest(WebServiceUrlConst.getUrl(),
                        WebServiceUrlConst.NAMESPACE, WebServiceUrlConst.CREATE_SALE_COUPON);
                request.addParam("saleNo", orderId);
                request.addParam("userNo", userNo);
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
        }).map(new Func1<NetResponse, CouponEntity>() {
            @Override
            public CouponEntity call(NetResponse netResponse) {
                String json = netResponse.getStringData();
                try {
                    ResultEntity<CouponEntity> entity = JsonUtils.fromJson(json,
                            new TypeToken<ResultEntity<CouponEntity>>(){}.getType());
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
    public Observable<VipPointRebateEntity> queryPointRebate(final QueryCardCommand command) {
        return Observable.create(new Observable.OnSubscribe<NetResponse>() {
            @Override
            public void call(Subscriber<? super NetResponse> subscriber) {
                NetRequest request = new NetRequest(WebServiceUrlConst.getUrl(),
                        WebServiceUrlConst.NAMESPACE, WebServiceUrlConst.QUERY_POINTS_REBATE);
                request.addParam("cdinfo", command.getTrackInfo());
                request.addParam("billno", command.getBillNo());
                request.addParam("shopId", command.getShopId());
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
        }).map(new Func1<NetResponse, VipPointRebateEntity>() {
            @Override
            public VipPointRebateEntity call(NetResponse netResponse) {
                String json = netResponse.getStringData();
                try {
                    ResultEntity<VipPointRebateEntity> entity = JsonUtils.fromJson(json,
                            new TypeToken<ResultEntity<VipPointRebateEntity>>(){}.getType());
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
    public Observable<CouponEntity> createPointCoupon(final VipPointsRebate rebate, final String userNo) {
        return Observable.create(new Observable.OnSubscribe<NetResponse>() {
            @Override
            public void call(Subscriber<? super NetResponse> subscriber) {
                NetRequest request = new NetRequest(WebServiceUrlConst.getUrl(),
                        WebServiceUrlConst.NAMESPACE, WebServiceUrlConst.CREATE_POINTS_REBATE_COUPON);
                request.addParam("ckcode", rebate.getCardNumber());
                request.addParam("cardtype", rebate.getCardType());
                request.addParam("userNo", userNo);
                request.addParam("cardjf", rebate.getPoints());
                request.addParam("kjjf", rebate.getRebatePoints());
                request.addParam("flje", rebate.getRebateAmount());
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
        }).map(new Func1<NetResponse, CouponEntity>() {
            @Override
            public CouponEntity call(NetResponse netResponse) {
                String json = netResponse.getStringData();
                try {
                    ResultEntity<CouponEntity> entity = JsonUtils.fromJson(json,
                            new TypeToken<ResultEntity<CouponEntity>>(){}.getType());
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
}
