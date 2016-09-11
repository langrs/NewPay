package com.unioncloud.newpay.domain.interactor.order;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.order.SaleOrder;
import com.unioncloud.newpay.domain.model.order.SaleOrderResult;
import com.unioncloud.newpay.domain.repository.SaleOrderRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/8.
 */
public class SaleOrderInteractor extends BaseInteractor<SaleOrderResult> {

    private SaleOrderRepository repository;
    private SaleOrder saleOrder;

    public SaleOrderInteractor(ExecutorProvider provider,
                               SaleOrder saleOrder,
                               SaleOrderRepository repository) {
        super(provider);
        this.saleOrder = saleOrder;
        this.repository = repository;
    }

    @Override
    protected Observable<SaleOrderResult> bindObservable() {
        return repository.submitSale(saleOrder);
    }


}
