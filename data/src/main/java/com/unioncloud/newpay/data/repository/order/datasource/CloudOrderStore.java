package com.unioncloud.newpay.data.repository.order.datasource;

import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.unioncloud.newpay.data.entity.ResultEntity;
import com.unioncloud.newpay.data.entity.order.SaleHeadEntity;
import com.unioncloud.newpay.data.entity.order.SaleItemEntity;
import com.unioncloud.newpay.data.entity.order.SaleOrderEntity;
import com.unioncloud.newpay.data.entity.order.SalePayEntity;
import com.unioncloud.newpay.data.entity.order.SaleResultEntity;
import com.unioncloud.newpay.data.exception.RemoteDataException;
import com.unioncloud.newpay.data.net.NetRequest;
import com.unioncloud.newpay.data.net.NetResponse;
import com.unioncloud.newpay.data.net.webservice.WebServiceEngine;
import com.unioncloud.newpay.data.repository.WebServiceUrlConst;
import com.unioncloud.newpay.data.utils.JsonUtils;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.order.QuerySaleOrderCommand;
import com.unioncloud.newpay.domain.model.order.SaleOrder;
import com.unioncloud.newpay.domain.model.order.SaleOrderHeader;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
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
 * Created by cwj on 16/8/16.
 */
public class CloudOrderStore implements OrderStore {

    @Override
    public Observable<String> getSerialNumber(PosInfo posInfo) {
        return Observable.just(null);
    }

    @Override
    public Observable<SaleResultEntity> submitSale(final SaleOrder saleOrder) {
        return Observable.create(new Observable.OnSubscribe<NetResponse>() {
            @Override
            public void call(Subscriber<? super NetResponse> subscriber) {
                NetRequest request = new NetRequest(WebServiceUrlConst.getUrl(),
                        WebServiceUrlConst.NAMESPACE, WebServiceUrlConst.SUBMIT_SALE);
                request.addParam("saleInfo", JsonUtils.toJson(mapper(saleOrder)));
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
        }).map(new Func1<NetResponse, SaleResultEntity>() {
            @Override
            public SaleResultEntity call(NetResponse netResponse) {
                String json = netResponse.getStringData();
                try {
                    ResultEntity<SaleResultEntity> entity = JsonUtils.fromJson(json,
                            new TypeToken<ResultEntity<SaleResultEntity>>(){}.getType());
                    if (entity != null && entity.isSuccess()) {
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

    private SaleOrderEntity mapper(SaleOrder order) {
        if (order == null) {
            return null;
        }
        SaleHeadEntity headEntity = new SaleHeadEntity();
        SaleOrderHeader header = order.getHeader();
        headEntity.setCompanyId(header.getCompanyId());
        headEntity.setShopId(header.getShopId());
        headEntity.setStoreId(header.getStoreId());
        headEntity.setPosId(header.getPosId());
        headEntity.setUserId(header.getUserId());
        headEntity.setSaleNo(header.getSaleNumber());
        headEntity.setSaleDate(header.getSaleDate());
        headEntity.setDealType(header.getDealType());
        headEntity.setSaleType(header.getSaleType());
        headEntity.setVipNo(header.getVipCardNumber());
        headEntity.setOriginalPoints(header.getPreviousPoints());
        headEntity.setSalePoints(header.getSalePoints());
        headEntity.setOriginalAmt(header.getOriginalAmount());
        headEntity.setSaleAmt(header.getSaleAmount());
        headEntity.setPayAmt(header.getPayAmount());
        headEntity.setDicAmt(header.getDiscountAmount());
        headEntity.setVipDicAmt(header.getVipDiscountAmount());
        headEntity.setChangeAmt(header.getChangedAmount());
        headEntity.setExcessAmt(header.getExcessAmount());
        headEntity.setIsTrain("");
        headEntity.setReason(header.getReason());
        headEntity.setRetAmt(header.getRefundAmount());
        headEntity.setEbillType(header.getEbillType());
        headEntity.setUpFlag(header.getUpFlag());
        headEntity.setUpData(header.getUpData());
        headEntity.setsSaleNo(header.getOriginalSaleNo());
//        退货授权人
        headEntity.setSalesId(header.getSalesId());
        Log.i("--headEntity--",headEntity.toString());

        ArrayList<SaleItemEntity> itemEntities = new ArrayList<>();
        for (CartItem cartItem : order.getItemList()) {
            itemEntities.add(mapperItem(cartItem));
        }

        ArrayList<SalePayEntity> payEntities = new ArrayList<>();
        for (PaymentUsed used : order.getPaymentUsedList()) {
            payEntities.add(mapperPay(used));
        }

        SaleOrderEntity orderEntity = new SaleOrderEntity();
        orderEntity.setSaleHead(headEntity);
        orderEntity.setSaleDetail(itemEntities);
        orderEntity.setSalePay(payEntities);
        return orderEntity;
    }

    private static SalePayEntity mapperPay(PaymentUsed used) {
        if (used == null) {
            return null;
        }
        SalePayEntity payEntity = new SalePayEntity();
        payEntity.setRowNo(String.valueOf(used.getRowNo()));
        payEntity.setBillNo(used.getRelationNumber());
        payEntity.setPaymodeId(used.getPaymentId());
        payEntity.setPayAmt(MoneyUtils.fenToString(used.getPayAmount()));
        payEntity.setExcessAmt(MoneyUtils.fenToString(used.getExcessAmount()));
        payEntity.setChangeAmt(MoneyUtils.fenToString(used.getChangeAmount()));
        payEntity.setCurrencyId(used.getCurrencyId());
        payEntity.setExchangeRate(used.getExchangeRate());
        return payEntity;
    }

    private static SaleItemEntity mapperItem(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }
        SaleItemEntity itemEntity = new SaleItemEntity();
        itemEntity.setRowNo(String.valueOf(cartItem.getRowNumber()));
        itemEntity.setItemId(cartItem.getProductId());
        itemEntity.setItemNo(cartItem.getProductNumber());
        itemEntity.setSaleNum(String.valueOf(cartItem.getQuantity()));
        itemEntity.setSalePrice(MoneyUtils.fenToString(cartItem.getSellUnitPrice()));
        itemEntity.setAllDistAmt(MoneyUtils.fenToString(cartItem.getDiscountAmount()));
        itemEntity.setItemSaleAmt(MoneyUtils.fenToString(cartItem.getSaleAmount()));
        itemEntity.setSalePoints(cartItem.getPoints());
        itemEntity.setVipDisc(cartItem.getVipDiscount());
        itemEntity.setVipDiscAmt(MoneyUtils.fenToString(cartItem.getVipDiscountAmount()));
        itemEntity.setPromDisc(cartItem.getPromDiscount());
        itemEntity.setPromDiscAmt(MoneyUtils.fenToString(cartItem.getPromDiscountAmount()));
        return itemEntity;
    }

    @Override
    public Observable<List<SaleOrderEntity>> querySaleOrder(final QuerySaleOrderCommand command) {
        return Observable.create(new Observable.OnSubscribe<NetResponse>() {
            @Override
            public void call(Subscriber<? super NetResponse> subscriber) {
                NetRequest request = new NetRequest(WebServiceUrlConst.getUrl(),
                        WebServiceUrlConst.NAMESPACE, WebServiceUrlConst.QUERY_SALE);
//                request.addParam("saleInfo", JsonUtils.toJson(command));
                request.addParam("shopId", command.getShopId());
                request.addParam("posId", command.getPosId());
                request.addParam("saleNo", command.getSaleNo());
                request.addParam("startDate", command.getStartDate());
                request.addParam("endDate", command.getEndDate());
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
        }).map(new Func1<NetResponse, List<SaleOrderEntity>>() {
            @Override
            public List<SaleOrderEntity> call(NetResponse netResponse) {
                String json = netResponse.getStringData();
                try {
                    ResultEntity<List<SaleOrderEntity>> entity = JsonUtils.fromJson(json,
                                new TypeToken<ResultEntity<List<SaleOrderEntity>>>(){}.getType());
                    if (entity != null && entity.isSuccess()) {
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
