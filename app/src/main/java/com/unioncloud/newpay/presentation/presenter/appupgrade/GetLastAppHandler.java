package com.unioncloud.newpay.presentation.presenter.appupgrade;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.appupgrade.GetLastAppInfoInteractor;
import com.unioncloud.newpay.domain.model.appupgrade.AppUpgrade;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/20.
 */
public class GetLastAppHandler extends UpdateHandler<AppUpgrade, GetLastAppHandler>
        implements Runnable {

    String packageName;

    public GetLastAppHandler(String packageName) {
        super(null, true);
        this.packageName = packageName;
    }

    @Override
    public void run() {
        GetLastAppInfoInteractor interactor = new GetLastAppInfoInteractor(
                PresenterUtils.getExecutorProvider(),
                packageName,
                PresenterUtils.getAppUpgradeRepository());
        interactor.execute(new Subscriber<AppUpgrade>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(AppUpgrade appUpgrade) {
                data = appUpgrade;
                onUpdateCompleted();
            }
        });
    }
}
