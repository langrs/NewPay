package com.unioncloud.pax;

/**
 * 当日消费撤销
 */
public class PaxSaleVoidRequest extends PaxRequest {

    private String voucherNo;

    public PaxSaleVoidRequest(String voucherNo) throws PaxPayException {
        super();
        PaxPreconditions.checkVoucherNumber(voucherNo);
        this.voucherNo = voucherNo;
        this.transType = PaxConstants.TRADE_VOID;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
}
