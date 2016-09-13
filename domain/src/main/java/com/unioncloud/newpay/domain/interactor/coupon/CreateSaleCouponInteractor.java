package com.unioncloud.newpay.domain.interactor.coupon;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.domain.repository.CouponRepository;

import rx.Observable;

/**
 * Created by cwj on 16/9/13.
 */
public class CreateSaleCouponInteractor extends BaseInteractor<Coupon> {

    CouponRepository repository;

    String orderId;
    String userNo;

    public CreateSaleCouponInteractor(ExecutorProvider provider,
                                      String orderId, String userNo,
                                      CouponRepository repository) {
        super(provider);
        this.orderId = orderId;
        this.userNo = userNo;
        this.repository = repository;
    }

    @Override
    protected Observable<Coupon> bindObservable() {
        return repository.saleReturnCoupon(orderId, userNo);
    }
}
