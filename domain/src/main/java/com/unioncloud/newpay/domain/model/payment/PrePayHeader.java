package com.unioncloud.newpay.domain.model.payment;

/**
 * Created by cwj on 16/8/8.
 */
public class PrePayHeader {
    private String companyId;
    private String shopId;
    private String posId;
    private String userId;  // 收银员ID
    private String vipType;
    private String vipNo;
    private String preSaleOrderNo;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVipType() {
        return vipType;
    }

    public void setVipType(String vipType) {
        this.vipType = vipType;
    }

    public String getVipNo() {
        return vipNo;
    }

    public void setVipNo(String vipNo) {
        this.vipNo = vipNo;
    }

    public String getPreSaleOrderNo() {
        return preSaleOrderNo;
    }

    public void setPreSaleOrderNo(String preSaleOrderNo) {
        this.preSaleOrderNo = preSaleOrderNo;
    }
}
