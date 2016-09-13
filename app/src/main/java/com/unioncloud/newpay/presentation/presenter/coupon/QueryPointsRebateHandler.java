package com.unioncloud.newpay.presentation.presenter.coupon;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.coupon.QueryPointsRebateInteractor;
import com.unioncloud.newpay.domain.model.erp.QueryCardCommand;
import com.unioncloud.newpay.domain.model.erp.VipPointsRebate;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/9/13.
 */
public class QueryPointsRebateHandler
        extends UpdateHandler<VipPointsRebate, QueryPointsRebateHandler>
        implements Runnable {

    QueryCardCommand command;

    public QueryPointsRebateHandler(QueryCardCommand command) {
        super(null, true);
        this.command = command;
    }

    @Override
    public void run() {
        QueryPointsRebateInteractor interactor = new QueryPointsRebateInteractor(
                PresenterUtils.getExecutorProvider(),
                command,
                PresenterUtils.getCouponRepository());
        interactor.execute(new Subscriber<VipPointsRebate>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(VipPointsRebate rebate) {
                data = rebate;
                onUpdateCompleted();
            }
        });
    }
}
