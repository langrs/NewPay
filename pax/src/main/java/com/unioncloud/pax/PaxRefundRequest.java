package com.unioncloud.pax;

/**
 * 隔日退货请求
 */
public class PaxRefundRequest extends PaxRequest {
    /**
     * 交易金额. 单位是cent(分)
     */
    private String transAmount;
    /**
     * 原参考号
     */
    private String origRefNo;
    /**
     * 原交易日期
     */
    private String origDate;

    public PaxRefundRequest(String transAmount, String origRefNo, String origDate) throws PaxPayException {
        super();
        this.transAmount = transAmount;
        PaxPreconditions.checkReferenceNumber(origRefNo);
        this.origRefNo = origRefNo;
        PaxPreconditions.checkTransDate(origDate);
        this.origDate = origDate;
        this.transType = PaxConstants.TRADE_REFUND;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public String getOrigRefNo() {
        return origRefNo;
    }

    public void setOrigRefNo(String origRefNo) {
        this.origRefNo = origRefNo;
    }

    public String getOrigDate() {
        return origDate;
    }

    public void setOrigDate(String origDate) {
        this.origDate = origDate;
    }
}
