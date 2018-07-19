package com.unioncloud.newpay.domain.interactor.testp;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.testp.Testp;
import com.unioncloud.newpay.domain.repository.TestpRepository;

import rx.Observable;

public class SaveTestpInteractor extends BaseInteractor {
    private Testp testp;
    private TestpRepository testpRepository;
    public SaveTestpInteractor(ExecutorProvider provider, Testp _testp, TestpRepository _testpRepository) {
        super(provider);
        this.testp = _testp;
        this.testpRepository = _testpRepository;
    }

    @Override
    protected Observable bindObservable() {
        return testpRepository.update(testp);
    }
}