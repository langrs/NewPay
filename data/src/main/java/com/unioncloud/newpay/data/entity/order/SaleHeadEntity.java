package com.unioncloud.newpay.data.entity.order;

/**
 * Created by cwj on 16/8/16.
 */
public class SaleHeadEntity {
    private String saleNo;          // 销售单号
    private String saleDate;        // 销售时间
    private String companyId;       // 公司ID
    private String shopId;          // 分店ID
    private String storeId;         // 商铺ID
    private String posId;           // 终端ID
    private String userId;          // 收银员ID

    private String dealType;        // 交易类型
    private String saleType;        // 销售类型 "01"正常销售单

    private String vipNo;
    private String originalPoints;  // 原积分
    private String salePoints;      // 本次销售积分

    private String originalAmt;     // 原价金额
    private String saleAmt;         // 应收总额
    private String payAmt;          // 实收总额
    private String dicAmt;          // 折扣总额(包含会员折扣)
    private String vipDicAmt;       // 会员折扣总额
    private String changeAmt;
    private String excessAmt;

    private String isTrain;         // 是否培训模式
    private String reason;          // 退货理由
    private String retAmt;          // 已退货总额
    private String ebillType;       // (是否领取)电子小票,"01"表示未领取
    private String upFlag;          // 上传标记
    private String upData;          // 上传日期;
    private String sSaleNo;         //退货单的话,对应原销售单号
    private String salesId;         //退货授权人

    public String getSaleNo() {
        return saleNo;
    }

    public void setSaleNo(String saleNo) {
        this.saleNo = saleNo;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public String getVipNo() {
        return vipNo;
    }

    public void setVipNo(String vipNo) {
        this.vipNo = vipNo;
    }

    public String getOriginalPoints() {
        return originalPoints;
    }

    public void setOriginalPoints(String originalPoints) {
        this.originalPoints = originalPoints;
    }

    public String getSalePoints() {
        return salePoints;
    }

    public void setSalePoints(String salePoints) {
        this.salePoints = salePoints;
    }

    public String getOriginalAmt() {
        return originalAmt;
    }

    public void setOriginalAmt(String originalAmt) {
        this.originalAmt = originalAmt;
    }

    public String getSaleAmt() {
        return saleAmt;
    }

    public void setSaleAmt(String saleAmt) {
        this.saleAmt = saleAmt;
    }

    public String getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(String payAmt) {
        this.payAmt = payAmt;
    }

    public String getDicAmt() {
        return dicAmt;
    }

    public void setDicAmt(String dicAmt) {
        this.dicAmt = dicAmt;
    }

    public String getVipDicAmt() {
        return vipDicAmt;
    }

    public void setVipDicAmt(String vipDicAmt) {
        this.vipDicAmt = vipDicAmt;
    }

    public String getIsTrain() {
        return isTrain;
    }

    public void setIsTrain(String isTrain) {
        this.isTrain = isTrain;
    }

    public String getChangeAmt() {
        return changeAmt;
    }

    public void setChangeAmt(String changeAmt) {
        this.changeAmt = changeAmt;
    }

    public String getExcessAmt() {
        return excessAmt;
    }

    public void setExcessAmt(String excessAmt) {
        this.excessAmt = excessAmt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRetAmt() {
        return retAmt;
    }

    public void setRetAmt(String retAmt) {
        this.retAmt = retAmt;
    }

    public String getEbillType() {
        return ebillType;
    }

    public void setEbillType(String ebillType) {
        this.ebillType = ebillType;
    }

    public String getUpFlag() {
        return upFlag;
    }

    public void setUpFlag(String upFlag) {
        this.upFlag = upFlag;
    }

    public String getUpData() {
        return upData;
    }

    public void setUpData(String upData) {
        this.upData = upData;
    }

    public String getsSaleNo() {
        return sSaleNo;
    }

    public void setsSaleNo(String sSaleNo) {
        this.sSaleNo = sSaleNo;
    }

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    @Override
    public String toString() {
        return "SaleHeadEntity{" +
                "saleNo='" + saleNo + '\'' +
                ", saleDate='" + saleDate + '\'' +
                ", companyId='" + companyId + '\'' +
                ", shopId='" + shopId + '\'' +
                ", storeId='" + storeId + '\'' +
                ", posId='" + posId + '\'' +
                ", userId='" + userId + '\'' +
                ", dealType='" + dealType + '\'' +
                ", saleType='" + saleType + '\'' +
                ", vipNo='" + vipNo + '\'' +
                ", originalPoints='" + originalPoints + '\'' +
                ", salePoints='" + salePoints + '\'' +
                ", originalAmt='" + originalAmt + '\'' +
                ", saleAmt='" + saleAmt + '\'' +
                ", payAmt='" + payAmt + '\'' +
                ", dicAmt='" + dicAmt + '\'' +
                ", vipDicAmt='" + vipDicAmt + '\'' +
                ", changeAmt='" + changeAmt + '\'' +
                ", excessAmt='" + excessAmt + '\'' +
                ", isTrain='" + isTrain + '\'' +
                ", reason='" + reason + '\'' +
                ", retAmt='" + retAmt + '\'' +
                ", ebillType='" + ebillType + '\'' +
                ", upFlag='" + upFlag + '\'' +
                ", upData='" + upData + '\'' +
                ", sSaleNo='" + sSaleNo + '\'' +
                ", salesId='" + salesId + '\'' +
                '}';
    }
}
