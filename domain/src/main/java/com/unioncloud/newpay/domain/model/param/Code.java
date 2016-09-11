package com.unioncloud.newpay.domain.model.param;

/**
 * Created by cwj on 16/7/1.
 */
public class Code {
    private String codeType;
    private String codeNumber;
    private String codeName;

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(String codeNumber) {
        this.codeNumber = codeNumber;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
}
