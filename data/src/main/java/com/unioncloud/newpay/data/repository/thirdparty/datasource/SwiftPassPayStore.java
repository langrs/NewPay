package com.unioncloud.newpay.data.repository.thirdparty.datasource;

import android.text.TextUtils;

import com.unioncloud.newpay.data.exception.RemoteDataException;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.SwiftPassConst;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.SwiftPassException;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.pay.PayRequest;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.pay.PayResult;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.pay.PayService;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.query.PayQueryService;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.query.QueryResult;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.refund.RefundOrderData;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.refund
        .RefundQueryRequest;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.refund
        .RefundQueryResult;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.refund
        .RefundQueryService;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.refund.RefundRequest;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.refund.RefundResult;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.refund.RefundService;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.thirdparty.RefundState;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyOrder;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyRefundOrder;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyRefundRecord;
import com.unioncloud.newpay.domain.model.thirdparty.TradeState;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by cwj on 16/8/18.
 */
public class SwiftPassPayStore implements ThirdPartyStore {

    @Override
    public Observable<ThirdPartyOrder> pay(final String orderId,
                                           final String code,
                                           final String attach,
                                           final int total,
                                           final String ip,
                                           final String body) {
        return Observable.create(new Observable.OnSubscribe<PayResult>() {
            @Override
            public void call(Subscriber<? super PayResult> subscriber) {
                PayRequest request = new PayRequest(orderId, body, attach, code, total, "", "", ip);
                PayService payTrans = new PayService();
                try {
                    PayResult payResult = payTrans.payBusiness(request);
                    subscriber.onNext(payResult);
                } catch (Exception e) {
                    if (e instanceof SwiftPassException) {
                        subscriber.onError(e);
                    } else {
                        subscriber.onError(new RemoteDataException("支付失败"));
                    }
                }
            }
        }).map(new Func1<PayResult, ThirdPartyOrder>() {
            @Override
            public ThirdPartyOrder call(PayResult payResult) {
                ThirdPartyOrder order = new ThirdPartyOrder();
                order.setOrderId(payResult.getOut_trade_no());
                order.setThirdOrderId(payResult.getTransaction_id());
                order.setBillNo(payResult.getTransaction_id());
                if (!TextUtils.isEmpty(payResult.getTotal_fee())) {
                    order.setTotalFee(Integer.valueOf(payResult.getTotal_fee()));
                }
                if (!TextUtils.isEmpty(payResult.getCoupon_fee())) {
                    order.setCouponFee(Integer.valueOf(payResult.getCoupon_fee()));
                }
                order.setAttach(payResult.getAttach());
                order.setDatetime(payResult.getTime_end());
                order.setTradeState(TradeState.SUCCESS);
                order.setThirdTradeName(SwiftPassConst.TradeType.findNameByKey(payResult.getTrade_type()));
                return order;
            }
        });
    }

    @Override
    public Observable<ThirdPartyOrder> queryOrder(final String orderId,
                                                  final String transactionId) {
        return Observable.create(new Observable.OnSubscribe<QueryResult>() {
            @Override
            public void call(Subscriber<? super QueryResult> subscriber) {
                PayQueryService payQueryTrans = new PayQueryService();
                try {
                    QueryResult queryResult = payQueryTrans.query(orderId, transactionId);
                    if (queryResult == null || queryResult.getStatus() == null) {
                        subscriber.onError(new RemoteDataException("查询返回失败"));
                    } else {
                        subscriber.onNext(queryResult);
                    }
                } catch (Exception e) {
                    if (e instanceof SwiftPassException) {
                        subscriber.onError(e);
                    } else {
                        subscriber.onError(new RemoteDataException("查询失败"));
                    }
                }
            }
        }).map(new Func1<QueryResult, ThirdPartyOrder>() {
            @Override
            public ThirdPartyOrder call(QueryResult queryResult) {
                ThirdPartyOrder order = new ThirdPartyOrder();
                order.setOrderId(orderId);
                if (TextUtils.isEmpty(queryResult.getTotal_fee())) {
                    order.setTotalFee(0);
                } else {
                    order.setTotalFee(Integer.valueOf(queryResult.getTotal_fee()));
                }
                if (TextUtils.isEmpty(queryResult.getCoupon_fee())) {
                    order.setCouponFee(0);
                } else {
                    order.setCouponFee(Integer.valueOf(queryResult.getCoupon_fee()));
                }
                order.setOrderId(queryResult.getOut_transaction_id());
                order.setThirdOrderId(queryResult.getTransaction_id());
                order.setBillNo(queryResult.getTransaction_id());
                order.setTradeState(TradeState.getTradeStateByTag(queryResult.getTrade_state()));
                order.setAttach(queryResult.getAttach());
                order.setDatetime(queryResult.getTime_end());
                order.setThirdTradeName(
                        SwiftPassConst.TradeType.findNameByKey(queryResult.getTrade_type()));
                return order;
            }
        });
    }

    @Override
    public Observable<ThirdPartyRefundRecord> queryRefund(final String orderId, final String refundOrderId,
                                                          final String transactionId, final String refundId) {
        return Observable.create(new Observable.OnSubscribe<RefundQueryResult>() {
            @Override
            public void call(Subscriber<? super RefundQueryResult> subscriber) {
                RefundQueryRequest request = new RefundQueryRequest(orderId, refundOrderId, transactionId, refundId);
                RefundQueryService service = new RefundQueryService();
                try {
                    RefundQueryResult result = service.queryBusiness(request);
                    subscriber.onNext(result);
                } catch (Exception e) {
                    if (e instanceof SwiftPassException) {
                        subscriber.onError(e);
                    } else {
                        subscriber.onError(new RemoteDataException("查询退款失败"));
                    }
                }
            }
        }).map(new Func1<RefundQueryResult, ThirdPartyRefundRecord>() {
            @Override
            public ThirdPartyRefundRecord call(RefundQueryResult refundQueryResult) {
                String orderId = refundQueryResult.getOut_trade_no();
                String thirdOrderId = refundQueryResult.getTransaction_id();

                ThirdPartyRefundRecord refundRecord = new ThirdPartyRefundRecord();
                refundRecord.setOrderId(orderId);
                refundRecord.setThirdOrderId(thirdOrderId);
                refundRecord.setRefundCount(refundQueryResult.getRefund_count());

                ArrayList<ThirdPartyRefundOrder> refundOrders = new ArrayList<>();
                for (RefundOrderData data : refundQueryResult.getRefundOrderList()) {
                    ThirdPartyRefundOrder refundOrder = new ThirdPartyRefundOrder();
                    refundOrder.setOrderId(orderId);
                    refundOrder.setThirdOrderId(thirdOrderId);
                    refundOrder.setRefundOrderId(data.getOutRefundNo());
                    refundOrder.setThirdRefundId(data.getRefundID());
                    refundOrder.setRefundFee(data.getRefundFee());
                    refundOrder.setCouponRefundFee(data.getCouponRefundFee());
                    refundOrder.setRefundChannel(data.getRefundChannel());
                    refundOrder.setRefundTime(data.getRefundTime());
                    refundOrder.setRefundState(RefundState.getRefundStateByTag(data.getRefundStatus()));
                    refundOrders.add(refundOrder);
                }
                refundRecord.setRefundOrderList(refundOrders);
                return refundRecord;
            }
        });
    }

    @Override
    public Observable<ThirdPartyRefundOrder> refund(final String orderId,
                                                    final String transactionId,
                                                    final int totalFee,
                                                    final String refundOrderId,
                                                    final int refundFee) {
        return Observable.create(new Observable.OnSubscribe<RefundResult>() {
            @Override
            public void call(Subscriber<? super RefundResult> subscriber) {
                RefundRequest request = new RefundRequest(orderId, refundOrderId, transactionId, totalFee, refundFee);
                RefundService service = new RefundService();
                try {
                    RefundResult result = service.refundBusiness(request);
                    subscriber.onNext(result);
                } catch (Exception e) {
                    if (e instanceof SwiftPassException) {
                        subscriber.onError(e);
                    } else {
                        subscriber.onError(new RemoteDataException("退款失败"));
                    }
                }
            }
        }).map(new Func1<RefundResult, ThirdPartyRefundOrder>() {
            @Override
            public ThirdPartyRefundOrder call(RefundResult refundResult) {
                ThirdPartyRefundOrder refundOrder = new ThirdPartyRefundOrder();
                refundOrder.setOrderId(refundResult.getOut_trade_no());
                refundOrder.setRefundOrderId(refundResult.getOut_refund_no());
                refundOrder.setThirdOrderId(refundResult.getTransaction_id());
                refundOrder.setThirdRefundId(refundResult.getRefund_id());
                refundOrder.setRefundFee(refundResult.getRefund_fee());
                refundOrder.setCouponRefundFee(refundResult.getCoupon_refund_fee());
                refundOrder.setRefundChannel(refundResult.getRefund_channel());
                refundOrder.setRefundState(RefundState.SUCCESS);
//                refundOrder.setRefundTime(refundResult.getr);
                return refundOrder;
            }
        });
    }
}
