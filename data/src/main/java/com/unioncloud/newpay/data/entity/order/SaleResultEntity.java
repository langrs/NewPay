package com.unioncloud.newpay.data.entity.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cwj on 16/8/16.
 */
public class SaleResultEntity {
    @SerializedName("saleResultHeadInfo")
    private SaleResultHead head;

    @SerializedName("saleResultCouponsesList")
    private List<CouponEntity> coupons;

    public SaleResultHead getHead() {
        return head;
    }

    public void setHead(SaleResultHead head) {
        this.head = head;
    }

    public List<CouponEntity> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponEntity> coupons) {
        this.coupons = coupons;
    }
}
