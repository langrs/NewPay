package com.unioncloud.newpay.domain.interactor.coupon;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.erp.QueryCardCommand;
import com.unioncloud.newpay.domain.model.erp.VipPointsRebate;
import com.unioncloud.newpay.domain.repository.CouponRepository;

import rx.Observable;

/**
 * Created by cwj on 16/9/13.
 */
public class QueryPointsRebateInteractor extends BaseInteractor<VipPointsRebate> {

    CouponRepository repository;

    QueryCardCommand command;

    public QueryPointsRebateInteractor(ExecutorProvider provider,
                                       QueryCardCommand command,
                                       CouponRepository repository) {
        super(provider);
        this.command = command;
        this.repository = repository;
    }

    @Override
    protected Observable<VipPointsRebate> bindObservable() {
        return repository.queryPointsRebate(command);
    }
}
