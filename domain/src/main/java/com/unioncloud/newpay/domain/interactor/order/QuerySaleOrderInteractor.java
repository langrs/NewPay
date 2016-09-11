package com.unioncloud.newpay.domain.interactor.order;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.order.QuerySaleOrderCommand;
import com.unioncloud.newpay.domain.model.order.SaleOrder;
import com.unioncloud.newpay.domain.repository.SaleOrderRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by cwj on 16/8/8.
 */
public class QuerySaleOrderInteractor extends BaseInteractor<List<SaleOrder>> {

    private SaleOrderRepository repository;
    private QuerySaleOrderCommand command;

    public QuerySaleOrderInteractor(ExecutorProvider provider,
                                    QuerySaleOrderCommand command,
                                    SaleOrderRepository repository) {
        super(provider);
        this.command = command;
        this.repository = repository;
    }

    @Override
    protected Observable<List<SaleOrder>> bindObservable() {
        return repository.querySaleOrder(command)
                .map(new Func1<List<SaleOrder>, List<SaleOrder>>() {
                    @Override
                    public List<SaleOrder> call(List<SaleOrder> orderList) {
                        Collections.sort(orderList, new Comparator<SaleOrder>() {
                            @Override
                            public int compare(SaleOrder lhs, SaleOrder rhs) {
                                String lOrderTime = lhs.getHeader().getSaleDate();
                                String rOrderTime = rhs.getHeader().getSaleDate();
                                return rOrderTime.compareTo(lOrderTime);
                            }
                        });
                        return orderList;
                    }
                });
    }
}
