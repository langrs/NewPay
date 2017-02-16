package com.unioncloud.pax;

public abstract class PaxRequest {
    protected String appId;
    protected String appName;
    /**
     * 交易类型
     */
    protected String transType;

    public PaxRequest() {
        this.appId = PaxConstants.APP_ID;
        this.appName = PaxConstants.APP_NAME;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }
}
