package com.unioncloud.newpay.domain.interactor.print;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.print.PrintOrderInfo;
import com.unioncloud.newpay.domain.repository.PrintRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/19.
 */
public class PrintOrderInteractor extends BaseInteractor<Boolean> {

    private PrintOrderInfo orderInfo;
    private PrintRepository repository;

    public PrintOrderInteractor(ExecutorProvider provider,
                                PrintOrderInfo orderInfo,
                                PrintRepository repository) {
        super(provider);
        this.orderInfo = orderInfo;
        this.repository = repository;
    }

    @Override
    protected Observable<Boolean> bindObservable() {
        return repository.printOrder(orderInfo);
    }
}
