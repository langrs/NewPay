package com.unioncloud.newpay.data.entity.checkout;

/**
 * Created by cwj on 16/7/1.
 */
public class CheckoutHeaderEntity {
    private String companyId;
    private String shopId;
    private String posId;
    private String dealType;        // 交易类型
    private String userId;          // 收银员ID
    private String vipType;         // 会员卡类型
    private String vipNumber;       // 会员卡号
    private String preSaleNumber;   // 预结算单号
    private String vipFlag;         // 是否允许会员折扣

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

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
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

    public String getVipNumber() {
        return vipNumber;
    }

    public void setVipNumber(String vipNumber) {
        this.vipNumber = vipNumber;
    }

    public String getPreSaleNumber() {
        return preSaleNumber;
    }

    public void setPreSaleNumber(String preSaleNumber) {
        this.preSaleNumber = preSaleNumber;
    }

    public String getVipFlag() {
        return vipFlag;
    }

    public void setVipFlag(String vipFlag) {
        this.vipFlag = vipFlag;
    }
}
