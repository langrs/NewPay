package com.unioncloud.newpay.domain.model.backcardpay.refund;

/**
 * Created by cwj on 16/7/19.
 */
public class BankcardRefundRequest {
    /** 主管操作员密码 */
    private String managerPwd;
    /** 原交易凭证号(6位) */
    private String oriVoucherNo;
    /** 原交易日期 */
    private String oriData;
    /** 交易金额 */
    private int transAmount;

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
