package com.unioncloud.newpay.domain.interactor.checkout;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.repository.SaleOrderRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/11.
 */
public class GetOrderSerialNumberInteractor extends BaseInteractor<String> {

    private PosInfo posInfo;
    private SaleOrderRepository repository;


    public GetOrderSerialNumberInteractor(ExecutorProvider provider,
                                          PosInfo posInfo,
                                          SaleOrderRepository repository) {
        super(provider);
        this.posInfo = posInfo;
        this.repository = repository;

    }

    @Override
    protected Observable<String> bindObservable() {
        return repository.getSerialNumber(posInfo);
    }
}
