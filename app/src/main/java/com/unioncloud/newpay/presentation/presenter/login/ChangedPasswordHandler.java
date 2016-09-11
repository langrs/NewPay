package com.unioncloud.newpay.presentation.presenter.login;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.login.ChangePasswordInteractor;
import com.unioncloud.newpay.domain.model.login.ChangePassword;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/9/5.
 */
public class ChangedPasswordHandler extends UpdateHandler<Boolean, ChangedPasswordHandler>
        implements Runnable {
    private ChangePassword changePassword;

    public ChangedPasswordHandler(ChangePassword changePassword) {
        super(Boolean.FALSE, true);
        this.changePassword = changePassword;
    }

    @Override
    public void run() {
        ChangePasswordInteractor interactor = new ChangePasswordInteractor(
                PresenterUtils.getExecutorProvider(),
                changePassword,
                PresenterUtils.getLoginRepository());
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
