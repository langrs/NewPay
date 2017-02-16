package com.unioncloud.pax;

public class PaxResponse {
    protected String appId;
    protected String rspCode;
    protected String rspMsg;

    // 以下字段在rspCode == PaxResponseCode.SUCCESS.code才返回
    protected String merchName;
    protected String merchId;
    protected String termId;
    protected String cardNo;
    protected String voucherNo;
    protected String batchNo;
    protected String isserCode;
    protected String acqCode;
    protected String authNo;
    protected String refNo;
    protected String transTime;
    protected String transDate;
    protected String transAmount;

    // 以下字段在进行退款交易时才返回
    protected String origAuthNo;
    protected String origVoucherNo;
    protected String origRefNo;
    protected String origdate;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspMsg() {
        return rspMsg;
    }

    public void setRspMsg(String rspMsg) {
        this.rspMsg = rspMsg;
    }

    /**
     * @return true, 本次交易成功; false, 交易失败
     */
    public boolean isSuccess() {
        return PaxResponseCode.SUCCESS.code.equals(rspCode);
    }

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    public String getMerchId() {
        return merchId;
    }

    public void setMerchId(String merchId) {
        this.merchId = merchId;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getIsserCode() {
        return isserCode;
    }

    public void setIsserCode(String isserCode) {
        this.isserCode = isserCode;
    }

    public String getAcqCode() {
        return acqCode;
    }

    public void setAcqCode(String acqCode) {
        this.acqCode = acqCode;
    }

    public String getAuthNo() {
        return authNo;
    }

    public void setAuthNo(String authNo) {
        this.authNo = authNo;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public String getOrigAuthNo() {
        return origAuthNo;
    }

    public void setOrigAuthNo(String origAuthNo) {
        this.origAuthNo = origAuthNo;
    }

    public String getOrigVoucherNo() {
        return origVoucherNo;
    }

    public void setOrigVoucherNo(String origVoucherNo) {
        this.origVoucherNo = origVoucherNo;
    }

    public String getOrigRefNo() {
        return origRefNo;
    }

    public void setOrigRefNo(String origRefNo) {
        this.origRefNo = origRefNo;
    }

    public String getOrigdate() {
        return origdate;
    }

    public void setOrigdate(String origdate) {
        this.origdate = origdate;
    }
}
