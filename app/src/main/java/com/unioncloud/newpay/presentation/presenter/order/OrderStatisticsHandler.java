package com.unioncloud.newpay.presentation.presenter.order;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.order.OrderStatisticsInteractor;
import com.unioncloud.newpay.domain.model.order.OrderStatistics;
import com.unioncloud.newpay.domain.model.order.QuerySaleOrderCommand;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import java.util.List;

import rx.Subscriber;

/**
 * Created by cwj on 16/9/4.
 */
public class OrderStatisticsHandler extends UpdateHandler<OrderStatistics, OrderStatisticsHandler>
    implements Runnable {

    private QuerySaleOrderCommand command;

    public OrderStatisticsHandler(QuerySaleOrderCommand command) {
        super(null, true);
        this.command = command;
    }

    @Override
    public void run() {
        OrderStatisticsInteractor interactor = new OrderStatisticsInteractor(
                PresenterUtils.getExecutorProvider(),
                command,
                PresenterUtils.getSaleOrderRepository());
        interactor.execute(new Subscriber<OrderStatistics>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(OrderStatistics statistics) {
                data = statistics;
                onUpdateCompleted();
            }
        });
    }
}
