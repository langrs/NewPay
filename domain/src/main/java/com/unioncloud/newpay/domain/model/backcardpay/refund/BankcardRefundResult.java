package com.unioncloud.newpay.domain.model.backcardpay.refund;

import com.unioncloud.newpay.domain.model.backcardpay.TransResult;

/**
 * Created by cwj on 16/7/19.
 */
public class BankcardRefundResult extends TransResult {
    private String voucherNo;
    private String referNo;
    private String batchNo;

    private int amount;

    public BankcardRefundResult() {
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getReferNo() {
        return referNo;
    }

    public void setReferNo(String referNo) {
        this.referNo = referNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
