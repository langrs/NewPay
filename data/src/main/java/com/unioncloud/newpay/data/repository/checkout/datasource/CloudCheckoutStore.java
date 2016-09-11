package com.unioncloud.newpay.data.repository.checkout.datasource;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.unioncloud.newpay.data.entity.ResultEntity;
import com.unioncloud.newpay.data.entity.checkout.CheckoutHeaderEntity;
import com.unioncloud.newpay.data.entity.checkout.CheckoutItemEntity;
import com.unioncloud.newpay.data.entity.checkout.CheckoutOrderEntity;
import com.unioncloud.newpay.data.exception.RemoteDataException;
import com.unioncloud.newpay.data.net.NetRequest;
import com.unioncloud.newpay.data.net.NetResponse;
import com.unioncloud.newpay.data.net.webservice.WebServiceEngine;
import com.unioncloud.newpay.data.repository.WebServiceUrlConst;
import com.unioncloud.newpay.data.utils.JsonUtils;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.erp.VipCard;
import com.unioncloud.newpay.domain.model.order.OrderType;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.utils.MoneyUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * Created by cwj on 16/8/15.
 */
public class CloudCheckoutStore implements CheckoutStore {

    @Override
    public Observable<List<CheckoutItemEntity>> calculateAmounts(final String orderId, final PosInfo posInfo,
                                                                 final VipCard vipCard,
                                                                 final OrderType orderType,
                                                                 final List<CartItem> itemList) {
        return Observable.create(new Observable.OnSubscribe<NetResponse>() {
            @Override
            public void call(Subscriber<? super NetResponse> subscriber) {
                NetRequest request = new NetRequest(WebServiceUrlConst.URL,
                        WebServiceUrlConst.NAMESPACE, WebServiceUrlConst.CHECKOUT_ACTION);
                CheckoutOrderEntity entity = createCheckoutOrder(orderId, posInfo, vipCard, orderType, itemList);
                request.addParam("preSaleInfo", JsonUtils.toJson(entity));
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
        }).map(new Func1<NetResponse, List<CheckoutItemEntity>>() {
            @Override
            public List<CheckoutItemEntity> call(NetResponse netResponse) {
                String json = netResponse.getStringData();
                try {
                    ResultEntity<List<CheckoutItemEntity>> entity = JsonUtils.fromJson(json,
                            new TypeToken<ResultEntity<List<CheckoutItemEntity>>>(){}.getType());
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

    private CheckoutOrderEntity createCheckoutOrder(String orderId, PosInfo posInfo,
                                                    VipCard vipCard,
                                                    OrderType orderType,
                                                    List<CartItem> itemList) {
        CheckoutHeaderEntity headEntity = new CheckoutHeaderEntity();
        headEntity.setCompanyId(posInfo.getCompanyId());
        headEntity.setShopId(posInfo.getShopId());
        headEntity.setPosId(posInfo.getPosId());
        headEntity.setDealType(orderType.getValue());
        headEntity.setUserId(posInfo.getUserId());
        headEntity.setPreSaleNumber(orderId);
        if (vipCard != null) {
            headEntity.setVipType(vipCard.getCardType());
            headEntity.setVipNumber(vipCard.getCardNumber());
            headEntity.setVipFlag("1");
        }

        ArrayList<CheckoutItemEntity> itemEntityList = new ArrayList<>(itemList.size());
        for (CartItem cartItem : itemList) {
            CheckoutItemEntity entity = mapperFromCart(cartItem);
            if (entity != null) {
                itemEntityList.add(entity);
            }
        }
        CheckoutOrderEntity orderEntity = new CheckoutOrderEntity();
        orderEntity.setHeader(headEntity);
        orderEntity.setItemList(itemEntityList);
        return orderEntity;
    }

    public static CheckoutItemEntity mapperFromCart(CartItem cartItem) {
        if (cartItem != null) {
            CheckoutItemEntity entity = new CheckoutItemEntity();
            entity.setRowNo(String.valueOf(cartItem.getRowNumber()));
            entity.setItemId(cartItem.getProductId());
            entity.setSalePrice(MoneyUtils.fenToString(cartItem.getSellUnitPrice()));
            entity.setSaleNum(String.valueOf(cartItem.getQuantity()));
            entity.setStoreId(cartItem.getStoreId());
            return entity;
        }
        return null;
    }
}
