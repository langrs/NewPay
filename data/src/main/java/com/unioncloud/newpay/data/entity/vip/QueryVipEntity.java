package com.unioncloud.newpay.data.entity.vip;

/**
 * Created by cwj on 16/8/17.
 */
public class QueryVipEntity {
    private String cdInfo;
    private String billno;
    private String shopId;

    public String getCdInfo() {
        return cdInfo;
    }

    public void setCdInfo(String cdInfo) {
        this.cdInfo = cdInfo;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
