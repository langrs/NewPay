package com.unioncloud.newpay.data.entity.paymethod;

/**
 * Created by cwj on 16/8/8.
 */
public class PayMethodGroupEntity {
    private String payviewId;
    private String parentId;
    private String payviewNo;
    private String payviewName;
    private String paymodeId;

    public String getPayviewId() {
        return payviewId;
    }

    public void setPayviewId(String payviewId) {
        this.payviewId = payviewId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPayviewNo() {
        return payviewNo;
    }

    public void setPayviewNo(String payviewNo) {
        this.payviewNo = payviewNo;
    }

    public String getPayviewName() {
        return payviewName;
    }

    public void setPayviewName(String payviewName) {
        this.payviewName = payviewName;
    }

    public String getPaymodeId() {
        return paymodeId;
    }

    public void setPaymodeId(String paymodeId) {
        this.paymodeId = paymodeId;
    }
}
