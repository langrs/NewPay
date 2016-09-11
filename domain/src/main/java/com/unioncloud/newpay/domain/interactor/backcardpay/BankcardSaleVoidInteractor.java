package com.unioncloud.newpay.domain.interactor.backcardpay;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundRequest;
import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundResult;
import com.unioncloud.newpay.domain.repository.BankcardTradeRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/23.
 */
public class BankcardSaleVoidInteractor extends BaseInteractor<BankcardRefundResult> {

    private BankcardRefundRequest request;
    private BankcardTradeRepository repository;

    public BankcardSaleVoidInteractor(ExecutorProvider provider,
                                      BankcardRefundRequest request,
                                      BankcardTradeRepository repository) {
        super(provider);
        this.request = request;
        this.repository = repository;
    }

    @Override
    protected Observable<BankcardRefundResult> bindObservable() {
        return repository.saleVoid(request);
    }
}
