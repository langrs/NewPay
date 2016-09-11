package com.unioncloud.newpay.domain.model.backcardpay.sale;

import com.unioncloud.newpay.domain.model.backcardpay.TransResult;

/**
 * Created by cwj on 16/7/19.
 */
public class BankcardSaleResult extends TransResult {
    /** 卡号(最长19位, 前6位和后4位显示) */
    private String cardNo;
    /** 交易凭证号 */
    private String voucherNo;
    /** 交易参考号 */
    private String referNo;
    /** 交易批次号 */
    private String batchNo;

    private int amount;

    public BankcardSaleResult() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
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

}
