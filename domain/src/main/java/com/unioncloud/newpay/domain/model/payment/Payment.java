package com.unioncloud.newpay.domain.model.payment;

/**
 * 支付方式
 */
public class Payment {
    private String paymentId;
    private String paymentNumber;
    private String paymentQy;       // 企业支付方式
    private String paymentName;

    private String isChange;        // 是否找零
    private String isPoint;         // 是否积分
    private String isInvoice;       // 是否开票
    private String isCoupon;        // 是否兑券
    private String isRefund;        // 是否退货
    private String isFlag;          // 是否有效
    private String currencyId;      // 币种ID
    private String isLocalCurrency; // 是否本币
    private String exchangeRate;    // 币种汇率

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public String getPaymentQy() {
        return paymentQy;
    }

    public void setPaymentQy(String paymentQy) {
        this.paymentQy = paymentQy;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getIsChange() {
        return isChange;
    }

    public void setIsChange(String isChange) {
        this.isChange = isChange;
    }

    public String getIsPoint() {
        return isPoint;
    }

    public void setIsPoint(String isPoint) {
        this.isPoint = isPoint;
    }

    public String getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(String isInvoice) {
        this.isInvoice = isInvoice;
    }

    public String getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(String isCoupon) {
        this.isCoupon = isCoupon;
    }

    public String getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(String isRefund) {
        this.isRefund = isRefund;
    }

    public String getIsFlag() {
        return isFlag;
    }

    public void setIsFlag(String isFlag) {
        this.isFlag = isFlag;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getIsLocalCurrency() {
        return isLocalCurrency;
    }

    public void setIsLocalCurrency(String isLocalCurrency) {
        this.isLocalCurrency = isLocalCurrency;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
