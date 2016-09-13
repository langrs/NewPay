package com.unioncloud.newpay.data.entity.coupon;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cwj on 16/8/17.
 */
public class CouponEntity {
    private String couponNo;
    private String couponValue;
    private String state;
    private String endDate;
    @SerializedName("gifttype")
    private String flag;

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public String getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(String couponValue) {
        this.couponValue = couponValue;
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
