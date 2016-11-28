package com.unioncloud.newpay.domain.model.erp;

import java.io.Serializable;

/**
 * 销售返券
 */
public class Coupon implements Serializable {
    private String couponNo;
    private String state;
    private String endDate;
    private String couponValue;
    private String flag;        // "1"－积分返利券, "2"-折扣券

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(String couponValue) {
        this.couponValue = couponValue;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public boolean isDiscountCoupon() {
        return "2".equals(flag);
    }

    public boolean isPointReturnCoupon() {
        return "1".equals(flag);
    }
}
