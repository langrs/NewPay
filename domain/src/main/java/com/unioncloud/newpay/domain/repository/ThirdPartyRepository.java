package com.unioncloud.newpay.domain.repository;

import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyOrder;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyRefundOrder;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyRefundRecord;

import rx.Observable;

/**
 * Created by cwj on 16/8/11.
 */
public interface ThirdPartyRepository {

    /**
     *
     * @param orderId   交易单号
     * @param attach    附加信息
     * @param total     支付金额(单位：分)
     * @return
     */
    Observable<ThirdPartyOrder> pay(String orderId, String code,
                                    String attach, int total,
                                    String ip,
                                    String body);

    /**
     *
     * @param orderId   支付单号
     * @return
     */
    Observable<ThirdPartyOrder> queryOrder(String orderId, String transactionId);

    /**
     *
     * @param orderId       原单号
     * @param refundOrderId 退款单号
     * @return
     */
    Observable<ThirdPartyRefundRecord> queryRefund(String orderId, String refundOrderId,
                                                  String transactionId, String refundId);

    /**
     *
     * @param orderId       原单号
     * @param transactionId (微信/支付宝)原单号
     * @param totalFee      原单总额
     * @param refundOrderId 退款单号
     * @param refundFee     退款金额
     * @return
     */
    Observable<ThirdPartyRefundOrder> refund(String orderId, String transactionId,
                                             int totalFee, String refundOrderId,
                                             int refundFee);
}
