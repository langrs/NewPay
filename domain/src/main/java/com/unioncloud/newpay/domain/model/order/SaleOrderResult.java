package com.unioncloud.newpay.domain.model.order;

import java.util.List;

/**
 * Created by cwj on 16/8/8.
 */
public class SaleOrderResult {
    private String saleNo;
    private String saleDate;
    private String salePoints;          //  销售积分
    private String originalPoints;       //  上期积分
    private List<Coupon> couponList;

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

    public String getSalePoints() {
        return salePoints;
    }

    public void setSalePoints(String salePoints) {
        this.salePoints = salePoints;
    }

    public String getOriginalPoints() {
        return originalPoints;
    }

    public void setOriginalPoints(String originalPoints) {
        this.originalPoints = originalPoints;
    }

    public List<Coupon> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<Coupon> couponList) {
        this.couponList = couponList;
    }
}
