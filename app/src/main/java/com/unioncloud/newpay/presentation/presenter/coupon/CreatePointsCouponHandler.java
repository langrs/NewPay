package com.unioncloud.newpay.presentation.presenter.coupon;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.coupon.CreatePointsCouponInteractor;
import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.domain.model.erp.VipPointsRebate;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/9/13.
 */
public class CreatePointsCouponHandler extends UpdateHandler<Coupon, CreatePointsCouponHandler>
        implements Runnable {

    VipPointsRebate rebateInfo;
    String userNo;

    public CreatePointsCouponHandler(VipPointsRebate rebateInfo, String userNo) {
        super(null, true);
        this.rebateInfo = rebateInfo;
        this.userNo = userNo;
    }

    @Override
    public void run() {
        CreatePointsCouponInteractor interactor = new CreatePointsCouponInteractor(
                PresenterUtils.getExecutorProvider(),
                rebateInfo, userNo,
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
