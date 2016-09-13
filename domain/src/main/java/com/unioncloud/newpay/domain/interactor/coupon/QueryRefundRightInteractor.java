package com.unioncloud.newpay.domain.interactor.coupon;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.repository.RightRepository;

import rx.Observable;

/**
 * Created by cwj on 16/9/13.
 */
public class QueryRefundRightInteractor extends BaseInteractor<Boolean> {

    RightRepository repository;

    String userNo;
    String password;

    public QueryRefundRightInteractor(ExecutorProvider provider,
                                      String userNo, String password,
                                      RightRepository repository) {
        super(provider);
        this.userNo = userNo;
        this.password = password;
        this.repository = repository;
    }

    @Override
    protected Observable<Boolean> bindObservable() {
        return repository.queryRefundRight(userNo, password);
    }
}
