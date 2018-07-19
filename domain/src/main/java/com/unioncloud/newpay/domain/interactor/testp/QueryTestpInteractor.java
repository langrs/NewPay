package com.unioncloud.newpay.domain.interactor.testp;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.testp.Testp;
import com.unioncloud.newpay.domain.repository.TestpRepository;

import rx.Observable;

public class QueryTestpInteractor extends BaseInteractor{

    Testp testp ;
    TestpRepository testpRepository;
    public QueryTestpInteractor(ExecutorProvider provider, Testp _testp, TestpRepository _testpRepository) {
        super(provider);
        this.testp = _testp;
        this.testpRepository = _testpRepository;
    }

    @Override
    protected Observable bindObservable() {
//        耗时操作,调用仓库方法被观察者
        return testpRepository.queryByNo(testp);
    }
}