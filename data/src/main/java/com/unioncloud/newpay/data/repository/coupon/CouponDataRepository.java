package com.unioncloud.newpay.data.repository.coupon;

import com.unioncloud.newpay.data.entity.coupon.GiftCouponEntity;
import com.unioncloud.newpay.data.repository.StoreFactory;
import com.unioncloud.newpay.domain.model.erp.GiftCoupon;
import com.unioncloud.newpay.domain.repository.CouponRepository;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by cwj on 16/9/6.
 */
public class CouponDataRepository implements CouponRepository {

    @Override
    public Observable<GiftCoupon> queryCoupon(String shopId, String couponNo) {
            return StoreFactory.getCouponStore().queryCoupon(shopId, couponNo)
                    .map(new Func1<GiftCouponEntity, GiftCoupon>() {
                        @Override
                        public GiftCoupon call(GiftCouponEntity giftCouponEntity) {
                            return mapper(giftCouponEntity);
                        }
                    });
    }

    private GiftCoupon mapper(GiftCouponEntity entity) {
        if (entity == null) {
            return null;
        }
        GiftCoupon coupon = new GiftCoupon();
        coupon.setCouponNumber(entity.getGiftCouponNo());
        coupon.setPrice(entity.getGiftPrice());
        coupon.setCouponStatus(entity.getGiftFlag());
        return coupon;
    }

}
