package com.unioncloud.newpay.domain.model.backcardpay;

/**
 * Created by cwj on 16/7/19.
 */
public abstract class TransResult {
//    /** 应用ID */
//    protected String appId;
    /** 交易类型 */
    protected String transType;
    /** 交易时间 */
    protected String dateTime;
    /** 应答码 */
    protected String respCode;
    /** 应答类别 */
    protected String respCategory;
    /** 应答信息 */
    protected String respMessage;

    public TransResult() {
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRespCode() {
        return respCode;
    }

    public String getRespCategory() {
        return respCategory;
    }

    public void setRespCategory(String respCategory) {
        this.respCategory = respCategory;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMessage() {
        return respMessage;
    }

    public void setRespMessage(String respMessage) {
        this.respMessage = respMessage;
    }

    public boolean isSuccess() {
        return "00".equals(respCode);
    }

}
