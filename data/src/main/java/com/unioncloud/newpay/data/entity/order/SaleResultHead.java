package com.unioncloud.newpay.data.entity.order;

/**
 * Created by cwj on 16/8/18.
 */
public class SaleResultHead {
    private String saleNo;          // 销售单号
    private String saleDate;        // 销售时间
    private String originalPoints;  // 原始积分
    private String salePoints;      // 销售积分

    public String getSaleNo() {
        return saleNo;
    }

    public void setSaleNo(String saleNo) {
        this.saleNo = saleNo;
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

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }
}
