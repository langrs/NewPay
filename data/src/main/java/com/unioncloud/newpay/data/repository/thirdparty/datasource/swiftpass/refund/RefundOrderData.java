package com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.refund;

/**
 * Created by cwj on 16/7/21.
 */
public class RefundOrderData {
    private String outRefundNo = "";    // 退款单号
    private String refundID = "";       // 威富通退款单号
    private String refundChannel = "";  // 退款渠道:IGINAL--原路退款;BALANCE--退回到余额

    private int refundFee = 0;          // 退款金额
    private int couponRefundFee = 0;    // 现金券退款金额
    private String refundTime;          // 退款时间
    private String refundStatus = "";   // 退款状态

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getRefundID() {
        return refundID;
    }

    public void setRefundID(String refundID) {
        this.refundID = refundID;
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

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }
}
