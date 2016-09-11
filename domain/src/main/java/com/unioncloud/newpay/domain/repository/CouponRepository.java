package com.unioncloud.newpay.domain.repository;

import com.unioncloud.newpay.domain.model.erp.GiftCoupon;

import rx.Observable;

/**
 * Created by cwj on 16/9/6.
 */
public interface CouponRepository {

    Observable<GiftCoupon> queryCoupon(String shopId, String couponNo);

}
