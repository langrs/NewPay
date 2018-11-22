package com.unioncloud.newpay.presentation.presenter.coupon;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.coupon.QueryWeChatCouponInteractor;
import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

public class QueryWeChatCouponHandler extends UpdateHandler<Coupon,QueryWeChatCouponHandler>
    implements Runnable{
    String code;
    String shopId;

    public QueryWeChatCouponHandler(String code,String shopId) {
        super( null,true);
        this.code = code;
        this.shopId = shopId;
    }

    @Override
    public void run() {
        QueryWeChatCouponInteractor interactor = new QueryWeChatCouponInteractor(
                PresenterUtils.getExecutorProvider(),
                code,shopId,
                PresenterUtils.getCouponRepository());
        interactor.execute(new Subscriber<Coupon>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(Coupon giftCoupon) {
                data = giftCoupon;
                onUpdateCompleted();
            }
        });
    }
}