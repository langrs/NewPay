package com.unioncloud.newpay.domain.model.order;

import java.io.Serializable;

/**
 * Created by cwj on 16/7/1.
 */
public class SaleOrderHeader implements Serializable {
    private String companyId;   // 公司ID
    private String shopId;
    private String storeId;
    private String posId;
    private String userId;
    private String saleNumber;
    private String saleDate;

    private String dealType;        // 交易类型: "01"-销售,"02"-退货
    private String saleType;        // "01"－正常单

    private String originalSaleNo;  // 原销售单号

    private String vipCardNumber;
    private String previousPoints;  // 原积分
    private String salePoints;      // 本次销售积分

    private String originalAmount;   // 原始总额
    private String saleAmount;      // 应收总额
    private String payAmount;       // 实收总额
    private String discountAmount;  // 折扣总额(包含会员折扣)
    private String vipDiscountAmount;// 会员折扣总额
    private String changedAmount;   // 找零金额
    private String excessAmount;    // 溢收金额

    private String isTrain;         // 是否培训模式
    private String reason;          // 退货理由
    private String refundAmount;    // 已退货总额
    private String ebillType;       // (是否领取)电子小票,"01"表示未领取
    private String upFlag;          // 上传标记
    private String upData;          // 上传日期;

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

    public String getSaleNumber() {
        return saleNumber;
    }

    public void setSaleNumber(String saleNumber) {
        this.saleNumber = saleNumber;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
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

    public String getOriginalSaleNo() {
        return originalSaleNo;
    }

    public void setOriginalSaleNo(String originalSaleNo) {
        this.originalSaleNo = originalSaleNo;
    }

    public String getVipCardNumber() {
        return vipCardNumber;
    }

    public void setVipCardNumber(String vipCardNumber) {
        this.vipCardNumber = vipCardNumber;
    }

    public String getPreviousPoints() {
        return previousPoints;
    }

    public void setPreviousPoints(String previousPoints) {
        this.previousPoints = previousPoints;
    }

    public String getSalePoints() {
        return salePoints;
    }

    public void setSalePoints(String salePoints) {
        this.salePoints = salePoints;
    }

    public String getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(String originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(String saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getVipDiscountAmount() {
        return vipDiscountAmount;
    }

    public void setVipDiscountAmount(String vipDiscountAmount) {
        this.vipDiscountAmount = vipDiscountAmount;
    }

    public String getChangedAmount() {
        return changedAmount;
    }

    public void setChangedAmount(String changedAmount) {
        this.changedAmount = changedAmount;
    }

    public String getExcessAmount() {
        return excessAmount;
    }

    public void setExcessAmount(String excessAmount) {
        this.excessAmount = excessAmount;
    }

    public String getIsTrain() {
        return isTrain;
    }

    public void setIsTrain(String isTrain) {
        this.isTrain = isTrain;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
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
}
