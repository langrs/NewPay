package com.unioncloud.newpay.data.repository.coupon;

import com.unioncloud.newpay.data.entity.coupon.CouponEntity;
import com.unioncloud.newpay.data.entity.coupon.VipPointRebateEntity;
import com.unioncloud.newpay.data.repository.StoreFactory;
import com.unioncloud.newpay.domain.model.erp.QueryCardCommand;
import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.domain.model.erp.VipPointsRebate;
import com.unioncloud.newpay.domain.repository.CouponRepository;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by cwj on 16/9/6.
 */
public class CouponDataRepository implements CouponRepository {

    @Override
    public Observable<Coupon> queryCoupon(String shopId, String couponNo) {
            return StoreFactory.getCouponStore().queryCoupon(shopId, couponNo)
                    .map(new Func1<CouponEntity, Coupon>() {
                        @Override
                        public Coupon call(CouponEntity entity) {
                            return mapper(entity);
                        }
                    });
    }

    private Coupon mapper(CouponEntity entity) {
        if (entity == null) {
            return null;
        }
        Coupon coupon = new Coupon();
        coupon.setCouponNo(entity.getCouponNo());
        coupon.setCouponValue(entity.getCouponValue());
        coupon.setFlag(entity.getFlag());
        return coupon;
    }

    @Override
    public Observable<Coupon> saleReturnCoupon(String saleNo, String userNo) {
        return StoreFactory.getCouponStore().createSaleCoupon(saleNo, userNo)
                .map(new Func1<CouponEntity, Coupon>() {
                    @Override
                    public Coupon call(CouponEntity entity) {
                        return mapper(entity);
                    }
                });
    }

    @Override
    public Observable<VipPointsRebate> queryPointsRebate(QueryCardCommand command) {
        return StoreFactory.getCouponStore().queryPointRebate(command)
                .map(new Func1<VipPointRebateEntity, VipPointsRebate>() {
                    @Override
                    public VipPointsRebate call(VipPointRebateEntity entity) {
                        return mapperRebate(entity);
                    }
                });
    }

    private VipPointsRebate mapperRebate(VipPointRebateEntity entity) {
        if (entity == null) {
            return null;
        }
        VipPointsRebate rebate = new VipPointsRebate();
        rebate.setCardNumber(entity.getCkcode());
        rebate.setCoBrandedNumber(entity.getLmcode());
        rebate.setPhoneNumber(entity.getCust_mobill());
        rebate.setName(entity.getCust_name());
        rebate.setPoints(entity.getCardjf());
        rebate.setCardType(entity.getCardtype());
        rebate.setCardStatus(entity.getCardstatus());
        rebate.setRebatePoints(entity.getKjjf());
        rebate.setRebateAmount(entity.getFlje());
        return rebate;
    }

    @Override
    public Observable<Coupon> pointsRebateCoupon(VipPointsRebate rebateInfo, String operatorNo) {
        return StoreFactory.getCouponStore().createPointCoupon(rebateInfo, operatorNo)
                .map(new Func1<CouponEntity, Coupon>() {
                    @Override
                    public Coupon call(CouponEntity entity) {
                        return mapper(entity);
                    }
                });
    }
}
