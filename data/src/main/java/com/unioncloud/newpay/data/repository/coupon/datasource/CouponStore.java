package com.unioncloud.newpay.data.repository.coupon.datasource;

import com.unioncloud.newpay.data.entity.coupon.CouponEntity;
import com.unioncloud.newpay.data.entity.coupon.VipPointRebateEntity;
import com.unioncloud.newpay.domain.model.erp.QueryCardCommand;
import com.unioncloud.newpay.domain.model.erp.VipPointsRebate;

import rx.Observable;

/**
 * Created by cwj on 16/9/6.
 */
public interface CouponStore {

    Observable<CouponEntity> queryCoupon(String shopId, String couponNo);

    Observable<CouponEntity> createSaleCoupon(String orderId, String userNo);

    Observable<VipPointRebateEntity> queryPointRebate(QueryCardCommand command);

    Observable<CouponEntity> createPointCoupon(VipPointsRebate rebate, String userNo);
}
