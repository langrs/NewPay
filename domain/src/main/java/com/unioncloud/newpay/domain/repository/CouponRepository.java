package com.unioncloud.newpay.domain.repository;

import com.unioncloud.newpay.domain.model.erp.QueryCardCommand;
import com.unioncloud.newpay.domain.model.erp.VipPointsRebate;
import com.unioncloud.newpay.domain.model.erp.Coupon;

import rx.Observable;

/**
 * Created by cwj on 16/9/6.
 */
public interface CouponRepository {

    /**
     * 购物券查询(验证)
     * @param shopId
     * @param couponNo
     * @return
     */
    Observable<Coupon> queryCoupon(String shopId, String couponNo);

    /**
     * 销售返利兑券
     * @return
     */
    Observable<Coupon> saleReturnCoupon(String saleNo, String userNo);

    /**
     * 查询会员卡的可用积分返利信息
     * @param command
     * @return
     */
    Observable<VipPointsRebate> queryPointsRebate(QueryCardCommand command);

    /**
     * 积分返利兑券
     * @param rebateInfo
     * @param operatorNo
     * @return
     */
    Observable<Coupon> pointsRebateCoupon(VipPointsRebate rebateInfo, String operatorNo);
}
