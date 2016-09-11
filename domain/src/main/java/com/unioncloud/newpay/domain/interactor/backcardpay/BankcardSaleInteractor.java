package com.unioncloud.newpay.domain.interactor.backcardpay;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleRequest;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleResult;
import com.unioncloud.newpay.domain.repository.BankcardTradeRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/17.
 */
public class BankcardSaleInteractor extends BaseInteractor<BankcardSaleResult> {

    private BankcardSaleRequest request;
    private BankcardTradeRepository repository;

    public BankcardSaleInteractor(ExecutorProvider provider,
                                  BankcardSaleRequest request,
                                  BankcardTradeRepository repository) {
        super(provider);
        this.request = request;
        this.repository = repository;
    }

    @Override
    protected Observable<BankcardSaleResult> bindObservable() {
        return repository.sale(request);
    }
}
