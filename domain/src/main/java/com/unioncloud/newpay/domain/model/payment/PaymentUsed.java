package com.unioncloud.newpay.domain.model.payment;

import java.io.Serializable;

/**
 * Created by cwj on 16/7/1.
 */
public class PaymentUsed implements Serializable {
    private String paymentId;
    private String paymentName;
    private String paymentQy;
    private String currencyId;      // 币种
    private String exchangeRate;    // 汇率
    private int payAmount;         // 支付
    private int excessAmount;      // 溢收
    private int changeAmount;      // 找零
    private String relationNumber;  // 关联号码
    private int rowNo;              // 行号

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentQy() {
        return paymentQy;
    }

    public void setPaymentQy(String paymentQy) {
        this.paymentQy = paymentQy;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public int getExcessAmount() {
        return excessAmount;
    }

    public void setExcessAmount(int excessAmount) {
        this.excessAmount = excessAmount;
    }

    public int getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(int changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getRelationNumber() {
        return relationNumber;
    }

    public void setRelationNumber(String relationNumber) {
        this.relationNumber = relationNumber;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

}
