package com.unioncloud.newpay.domain.model.thirdparty;

/**
 * Created by cwj on 16/8/11.
 */
public class ThirdPartyRefundOrder {

    private String orderId;         // 原支付单号
    private String refundOrderId;   // 商户退款单号
    private String thirdOrderId;    // 第三方原单号
    private String thirdRefundId;   // 第三方退款单号
    private String refundChannel;   // 退款渠道
    private int refundFee;          // 退款金额
    private int couponRefundFee;    // 退还现金券
    private String refundTime;      // 退款时间

    private RefundState refundState;// 退款状态

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRefundOrderId() {
        return refundOrderId;
    }

    public void setRefundOrderId(String refundOrderId) {
        this.refundOrderId = refundOrderId;
    }

    public String getThirdOrderId() {
        return thirdOrderId;
    }

    public void setThirdOrderId(String thirdOrderId) {
        this.thirdOrderId = thirdOrderId;
    }

    public String getThirdRefundId() {
        return thirdRefundId;
    }

    public void setThirdRefundId(String thirdRefundId) {
        this.thirdRefundId = thirdRefundId;
    }

    public String getRefundChannel() {
        return refundChannel;
    }

    public void setRefundChannel(String refundChannel) {
        this.refundChannel = refundChannel;
    }

    public int getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(int refundFee) {
        this.refundFee = refundFee;
    }

    public int getCouponRefundFee() {
        return couponRefundFee;
    }

    public void setCouponRefundFee(int couponRefundFee) {
        this.couponRefundFee = couponRefundFee;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public RefundState getRefundState() {
        return refundState;
    }

    public void setRefundState(RefundState refundState) {
        this.refundState = refundState;
    }
}
