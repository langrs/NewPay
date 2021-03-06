package com.unioncloud.newpay.domain.interactor.coupon;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.domain.repository.CouponRepository;

import rx.Observable;

/**
 * Created by cwj on 16/9/6.
 */
public class QueryCouponInteractor extends BaseInteractor<Coupon> {
    CouponRepository repository;

    String shopId;
    String couponNo;

    public QueryCouponInteractor(ExecutorProvider provider,
                                 String shopId,
                                 String couponNo,
                                 CouponRepository repository) {
        super(provider);
        this.shopId = shopId;
        this.couponNo = couponNo;
        this.repository = repository;
    }

    @Override
    protected Observable<Coupon> bindObservable() {
        return repository.queryCoupon(shopId, couponNo);
    }
}
