package com.unioncloud.newpay.domain.model.erp;

import java.io.Serializable;

/**
 * Created by cwj on 16/8/8.
 */
public class GiftCoupon implements Serializable {
    private String couponNumber;
    private String price;
    private String couponStatus;

    public String getCouponNumber() {
        return couponNumber;
    }

    public void setCouponNumber(String couponNumber) {
        this.couponNumber = couponNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(String couponStatus) {
        this.couponStatus = couponStatus;
    }
}
