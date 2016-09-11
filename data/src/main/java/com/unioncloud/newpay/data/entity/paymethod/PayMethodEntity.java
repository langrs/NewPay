package com.unioncloud.newpay.data.entity.paymethod;

/**
 * Created by cwj on 16/8/8.
 */
public class PayMethodEntity {
    private String paymodeId;
    private String paymodeNo;
    private String paymodeQy;
    private String paymodeName;
    private String isChange;
    private String isPoint;     // 是否积分
    private String isInvoice;   // 是否开票
    private String isCoupon;    // 是否兑券
    private String isRet;   // 是否退货
    private String isFlag;  // 是否有效
    private String currencyId;
    private String isLocalCurrency;
    private String exchangeRate;

    public String getPaymodeId() {
        return paymodeId;
    }

    public void setPaymodeId(String paymodeId) {
        this.paymodeId = paymodeId;
    }

    public String getPaymodeNo() {
        return paymodeNo;
    }

    public void setPaymodeNo(String paymodeNo) {
        this.paymodeNo = paymodeNo;
    }

    public String getPaymodeQy() {
        return paymodeQy;
    }

    public void setPaymodeQy(String paymodeQy) {
        this.paymodeQy = paymodeQy;
    }

    public String getPaymodeName() {
        return paymodeName;
    }

    public void setPaymodeName(String paymodeName) {
        this.paymodeName = paymodeName;
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

    public String getIsRet() {
        return isRet;
    }

    public void setIsRet(String isRet) {
        this.isRet = isRet;
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
