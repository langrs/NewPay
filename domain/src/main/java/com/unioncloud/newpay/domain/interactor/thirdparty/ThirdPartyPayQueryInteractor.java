package com.unioncloud.newpay.domain.interactor.thirdparty;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyOrder;
import com.unioncloud.newpay.domain.repository.ThirdPartyRepository;

import rx.Observable;

public class ThirdPartyPayQueryInteractor extends BaseInteractor<ThirdPartyOrder> {
    String orderId;
    String thirdPartyOrderId;
    ThirdPartyRepository repository;

    public ThirdPartyPayQueryInteractor(ExecutorProvider provider,
                                        String orderId, String thirdPartyOrderId,
                                        ThirdPartyRepository repository) {
        super(provider);
        this.orderId = orderId;
        this.thirdPartyOrderId = thirdPartyOrderId;
        this.repository = repository;
    }

    @Override
    protected Observable<ThirdPartyOrder> bindObservable() {
        return repository.queryOrder(orderId, thirdPartyOrderId);
    }
}
