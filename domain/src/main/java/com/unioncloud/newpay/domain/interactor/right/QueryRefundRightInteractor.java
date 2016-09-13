package com.unioncloud.newpay.domain.interactor.right;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.repository.RightRepository;

import rx.Observable;

/**
 * Created by cwj on 16/9/13.
 */
public class QueryRefundRightInteractor extends BaseInteractor<Boolean> {

    RightRepository repository;

    String number;
    String password;

    public QueryRefundRightInteractor(ExecutorProvider provider,
                                      String number, String password,
                                      RightRepository repository) {
        super(provider);
        this.number = number;
        this.password = password;
        this.repository = repository;
    }

    @Override
    protected Observable<Boolean> bindObservable() {
        return repository.queryRefundRight(number, password);
    }
}
