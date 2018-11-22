package com.unioncloud.newpay.domain.interactor.coupon;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.domain.repository.CouponRepository;

import rx.Observable;

/**
 * Created by cwj on 16/9/6.
 */
public class QueryWeChatCouponInteractor extends BaseInteractor<Coupon> {
    CouponRepository repository;

    String code;
    String shopId;

    public QueryWeChatCouponInteractor(ExecutorProvider provider,
                                       String code,
                                       String shopId,
                                       CouponRepository repository) {
        super(provider);
        this.code = code;
        this.shopId = shopId;
        this.repository = repository;
    }

    @Override
    protected Observable<Coupon> bindObservable() {
        return repository.consumeWeChatCoupon(code,shopId);
    }
}
