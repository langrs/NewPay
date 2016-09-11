package com.unioncloud.newpay.data.repository.bankcardpay.datasource;

import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundRequest;

/**
 * Created by cwj on 16/8/22.
 */
public class PaxSaleVoidRequest extends PaxAbsRequest {

    private String managerPwd;
    private String oriVoucherNo;

    public PaxSaleVoidRequest(BankcardRefundRequest request) {
        this.managerPwd = request.getManagerPwd();
        this.oriVoucherNo = request.getOriVoucherNo();
    }

    public String getManagerPwd() {
        return managerPwd;
    }

    public void setManagerPwd(String managerPwd) {
        this.managerPwd = managerPwd;
    }

    public String getOriVoucherNo() {
        return oriVoucherNo;
    }

    public void setOriVoucherNo(String oriVoucherNo) {
        this.oriVoucherNo = oriVoucherNo;
    }
}
