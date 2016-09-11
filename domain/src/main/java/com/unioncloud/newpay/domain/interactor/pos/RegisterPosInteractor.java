package com.unioncloud.newpay.domain.interactor.pos;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.pos.PosRegister;
import com.unioncloud.newpay.domain.repository.PosRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/8.
 */
public class RegisterPosInteractor extends BaseInteractor<Boolean> {

    private PosRepository repository;
    private PosRegister registerInfo;

    public RegisterPosInteractor(ExecutorProvider provider,
                                 PosRegister registerInfo,
                                 PosRepository repository) {
        super(provider);
        this.registerInfo = registerInfo;
        this.repository = repository;
    }

    @Override
    protected Observable<Boolean> bindObservable() {
        return repository.register(registerInfo);
    }
}
