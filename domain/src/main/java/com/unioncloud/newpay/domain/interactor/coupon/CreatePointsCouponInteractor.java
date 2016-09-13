package com.unioncloud.newpay.domain.interactor.coupon;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.domain.model.erp.VipPointsRebate;
import com.unioncloud.newpay.domain.repository.CouponRepository;

import rx.Observable;

/**
 * Created by cwj on 16/9/13.
 */
public class CreatePointsCouponInteractor extends BaseInteractor<Coupon> {
    CouponRepository repository;

    VipPointsRebate rebateInfo;
    String userNo;

    public CreatePointsCouponInteractor(ExecutorProvider provider,
                                        VipPointsRebate rebateInfo,
                                        String userNo,
                                        CouponRepository repository) {
        super(provider);
        this.rebateInfo = rebateInfo;
        this.userNo = userNo;
        this.repository = repository;
    }

    @Override
    protected Observable<Coupon> bindObservable() {
        return repository.pointsRebateCoupon(rebateInfo, userNo);
    }
}
