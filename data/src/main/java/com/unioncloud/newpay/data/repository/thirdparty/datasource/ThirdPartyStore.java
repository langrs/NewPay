package com.unioncloud.newpay.data.repository.thirdparty.datasource;

import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyOrder;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyRefundOrder;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyRefundRecord;

import rx.Observable;

/**
 * Created by cwj on 16/8/18.
 */
public interface ThirdPartyStore {

    /**
     *
     * @param orderId   订单号
     * @param code      扫描得到的用户授权码
     * @param attach    附加信息
     * @param total     总额(单位分)
     * @param ip        IP地址
     * @param body
     * @return
     */
    Observable<ThirdPartyOrder> pay(String orderId, String code, String attach, int total, String ip, String body);

    /**
     *
     * @param orderId   定单号/退款单号
     * @param transactionId 第三方交易单号/第三方退款单号
     * @return
     */
    Observable<ThirdPartyOrder> queryOrder(String orderId, String transactionId);

    /**
     *
     * @param orderId       原订单号
     * @param refundOrderId 退款单号
     * @param transactionId 第三方原交易单号
     * @param refundId      第三方退款单号
     * @return
     */
    Observable<ThirdPartyRefundRecord> queryRefund(String orderId, String refundOrderId,
                                                   String transactionId, String refundId);

    /**
     * 原订单号,第三方交易单号.2选1传入
     * @param orderId       原订单号
     * @param transactionId 第三方原交易单号
     * @param totalFee      原单总额
     * @param refundOrderId 退款单号
     * @param refundFee     退款金额
     * @return
     */
    Observable<ThirdPartyRefundOrder> refund(String orderId, String transactionId,
                                             int totalFee, String refundOrderId,
                                             int refundFee);


}
