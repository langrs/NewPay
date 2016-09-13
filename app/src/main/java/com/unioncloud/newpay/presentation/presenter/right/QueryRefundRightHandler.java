package com.unioncloud.newpay.presentation.presenter.right;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.coupon.QueryRefundRightInteractor;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/9/13.
 */
public class QueryRefundRightHandler extends UpdateHandler<Boolean, QueryRefundRightHandler>
implements Runnable {

    String userNo;
    String password;

    public QueryRefundRightHandler(String userNo, String password) {
        super(Boolean.FALSE, true);
        this.userNo = userNo;
        this.password = password;
    }

    @Override
    public void run() {
        QueryRefundRightInteractor interactor = new QueryRefundRightInteractor(
                PresenterUtils.getExecutorProvider(),
                userNo, password,
                PresenterUtils.getRightRepository());
        interactor.execute(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                data = aBoolean;
                onUpdateCompleted();
            }
        });
    }
}
