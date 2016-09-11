package com.unioncloud.newpay.data.repository.bankcardpay.datasource;

/**
 * Created by cwj on 16/9/1.
 */
public abstract class PaxAbsRequest {
    protected String appId;
    protected String appName;

    public PaxAbsRequest() {
        appId = "com.unioncloud.Payment";
        appName = "智云支付";
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
}
