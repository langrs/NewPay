package com.unioncloud.newpay.domain.model.param;

/**
 * Created by cwj on 16/7/1.
 */
public class ExtraParam {
    private String paramId;
    private String paramNumber;
    private String paramName;
    private String paramValue;

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getParamNumber() {
        return paramNumber;
    }

    public void setParamNumber(String paramNumber) {
        this.paramNumber = paramNumber;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
