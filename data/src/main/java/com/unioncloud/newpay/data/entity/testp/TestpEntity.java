package com.unioncloud.newpay.data.entity.testp;

public class TestpEntity {
    private String ckcode;
    private String custAddr;
    private String custName;
    private String mkcode;
    private String custMobile;

    public String getCkcode() {
        return ckcode;
    }

    public void setCkcode(String ckcode) {
        this.ckcode = ckcode;
    }

    public String getCustAddr() {
        return custAddr;
    }

    public void setCustAddr(String custAddr) {
        this.custAddr = custAddr;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getMkcode() {
        return mkcode;
    }

    public void setMkcode(String mkcode) {
        this.mkcode = mkcode;
    }

    public String getCustMobile() {
        return custMobile;
    }

    public void setCustMobile(String custMobile) {
        this.custMobile = custMobile;
    }

    @Override
    public String toString() {
        return "TestpEntity{" +
                "ckcode='" + ckcode + '\'' +
                ", custAddr='" + custAddr + '\'' +
                ", custName='" + custName + '\'' +
                ", mkcode='" + mkcode + '\'' +
                ", custMobile='" + custMobile + '\'' +
                '}';
    }
}