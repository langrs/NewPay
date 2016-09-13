package com.unioncloud.newpay.presentation.presenter.coupon;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.coupon.CreateSaleCouponInteractor;
import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/9/13.
 */
public class CreateSaleCouponHandler extends UpdateHandler<Coupon, CreateSaleCouponHandler>
        implements Runnable {

    String orderId;
    String userNo;

    public CreateSaleCouponHandler(String orderId, String userNo) {
        super(null, true);
        this.orderId = orderId;
        this.userNo = userNo;
    }

    @Override
    public void run() {
        CreateSaleCouponInteractor interactor = new CreateSaleCouponInteractor(
                PresenterUtils.getExecutorProvider(),
                orderId, userNo,
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
            public void onNext(Coupon coupon) {
                data = coupon;
                onUpdateCompleted();
            }
        });
    }
}
