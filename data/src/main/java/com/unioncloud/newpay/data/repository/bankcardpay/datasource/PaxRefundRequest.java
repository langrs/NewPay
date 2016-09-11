package com.unioncloud.newpay.data.repository.bankcardpay.datasource;

import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundRequest;

/**
 * Created by cwj on 16/9/1.
 */
public class PaxRefundRequest extends PaxAbsRequest {

    private String managerPwd;
    private String oriVoucherNo;
    private String oriData;
    private int transAmount;

    public PaxRefundRequest(BankcardRefundRequest request) {
        this.managerPwd = request.getManagerPwd();
        this.oriVoucherNo = request.getOriVoucherNo();
        this.oriData = request.getOriData();
        this.transAmount = request.getTransAmount();
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

    public String getOriData() {
        return oriData;
    }

    public void setOriData(String oriData) {
        this.oriData = oriData;
    }

    public int getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(int transAmount) {
        this.transAmount = transAmount;
    }
}
