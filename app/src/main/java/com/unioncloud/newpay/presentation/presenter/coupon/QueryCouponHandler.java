package com.unioncloud.newpay.presentation.presenter.coupon;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.coupon.QueryCouponInteractor;
import com.unioncloud.newpay.domain.model.erp.GiftCoupon;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/9/6.
 */
public class QueryCouponHandler extends UpdateHandler<GiftCoupon, QueryCouponHandler>
    implements Runnable {

    private String shopId;
    private String couponNo;

    public QueryCouponHandler(String shopId, String couponNo) {
        super(null, true);
        this.shopId = shopId;
        this.couponNo = couponNo;
    }

    @Override
    public void run() {
        QueryCouponInteractor interactor = new QueryCouponInteractor(
                PresenterUtils.getExecutorProvider(),
                shopId, couponNo,
                PresenterUtils.getCouponRepository());
        interactor.execute(new Subscriber<GiftCoupon>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(GiftCoupon giftCoupon) {
                data = giftCoupon;
                onUpdateCompleted();
            }
        });
    }
}
