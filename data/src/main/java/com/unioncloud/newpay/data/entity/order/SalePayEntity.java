package com.unioncloud.newpay.data.entity.order;

/**
 * Created by cwj on 16/8/16.
 */
public class SalePayEntity {
    private String rowNo;
    private String billNo;          // 单据号码
    private String paymodeId;       // 支付方式
    private String excessAmt;       // 溢收金额
    private String payAmt;          // 支付金额
    private String changeAmt;      // 找零金额
    private String currencyId;      // 币种ID
    private String exchangeRate;    // 汇率

    public String getRowNo() {
        return rowNo;
    }

    public void setRowNo(String rowNo) {
        this.rowNo = rowNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getPaymodeId() {
        return paymodeId;
    }

    public void setPaymodeId(String paymodeId) {
        this.paymodeId = paymodeId;
    }

    public String getExcessAmt() {
        return excessAmt;
    }

    public void setExcessAmt(String excessAmt) {
        this.excessAmt = excessAmt;
    }

    public String getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(String payAmt) {
        this.payAmt = payAmt;
    }

    public String getChangeAmt() {
        return changeAmt;
    }

    public void setChangeAmt(String changeAmt) {
        this.changeAmt = changeAmt;
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
}
