package com.unioncloud.newpay.domain.model.thirdparty;

import java.io.Serializable;

/**
 * Created by cwj on 16/8/11.
 */
public class ThirdPartyOrder implements Serializable {

    private String thirdTradeName;  // 第3方交易的名称

    private String orderId;         // 交易单号
    private String thirdOrderId;    // 第三方的支付单号(威富通)
    private String attach;          // 支付时的附加信息(微信/支付宝支付方式名称)
    private String body;            // 支付时,显示给顾客的提示信息(商场＋店铺名称)
    private int totalFee;           // 总金额(单位是分)
    private int couponFee;          // 优惠金额
    private String billNo;          // 关联帐号(第三方支付微信/支付宝时的交易单号)
    private String datetime;        // 交易时间

    private TradeState tradeState;  // 支付交易状态

    public String getThirdTradeName() {
        return thirdTradeName;
    }

    public void setThirdTradeName(String thirdTradeName) {
        this.thirdTradeName = thirdTradeName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getThirdOrderId() {
        return thirdOrderId;
    }

    public void setThirdOrderId(String thirdOrderId) {
        this.thirdOrderId = thirdOrderId;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public int getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(int couponFee) {
        this.couponFee = couponFee;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public TradeState getTradeState() {
        return tradeState;
    }

    public void setTradeState(TradeState tradeState) {
        this.tradeState = tradeState;
    }

}
