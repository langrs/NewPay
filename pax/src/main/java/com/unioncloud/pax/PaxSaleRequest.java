package com.unioncloud.pax;

/**
 * 消费请求
 */
public class PaxSaleRequest extends PaxRequest {
    /**
     * 交易金额. 单位是cent(分), 最大长度12位.
     */
    private String transAmount;

    public PaxSaleRequest(String transAmount) throws PaxPayException {
        super();
        PaxPreconditions.checkAmount(transAmount);
        this.transAmount = transAmount;
        this.transType = PaxConstants.TRADE_SALE;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }
}
