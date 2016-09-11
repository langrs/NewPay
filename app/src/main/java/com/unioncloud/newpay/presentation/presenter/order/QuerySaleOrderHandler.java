package com.unioncloud.newpay.presentation.presenter.order;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.order.QuerySaleOrderInteractor;
import com.unioncloud.newpay.domain.model.order.QuerySaleOrderCommand;
import com.unioncloud.newpay.domain.model.order.SaleOrder;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import java.util.List;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/24.
 */
public class QuerySaleOrderHandler extends UpdateHandler<List<SaleOrder>, QuerySaleOrderHandler>
        implements Runnable {

    private QuerySaleOrderCommand command;

    public QuerySaleOrderHandler(QuerySaleOrderCommand command) {
        super(null, true);
        this.command = command;
    }

    @Override
    public void run() {
        QuerySaleOrderInteractor interactor = new QuerySaleOrderInteractor(
                PresenterUtils.getExecutorProvider(),
                command,
                PresenterUtils.getSaleOrderRepository());
        interactor.execute(new Subscriber<List<SaleOrder>>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(List<SaleOrder> saleOrders) {
                data = saleOrders;
                onUpdateCompleted();
            }
        });
    }
}
