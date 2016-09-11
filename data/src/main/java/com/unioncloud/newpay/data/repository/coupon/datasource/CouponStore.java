package com.unioncloud.newpay.data.repository.coupon.datasource;

import com.unioncloud.newpay.data.entity.coupon.GiftCouponEntity;

import rx.Observable;

/**
 * Created by cwj on 16/9/6.
 */
public interface CouponStore {

    Observable<GiftCouponEntity> queryCoupon(String shopId, String couponNo);

}
