package com.unioncloud.newpay.domain.model.testp;

import java.io.Serializable;

public class Testp implements Serializable{
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

    @Override
    public String toString() {
        return "Testp{" +
                "ckcode='" + ckcode + '\'' +
                ", custAddr='" + custAddr + '\'' +
                ", custName='" + custName + '\'' +
                ", mkcode='" + mkcode + '\'' +
                ", custMobile='" + custMobile + '\'' +
                '}';
    }
}