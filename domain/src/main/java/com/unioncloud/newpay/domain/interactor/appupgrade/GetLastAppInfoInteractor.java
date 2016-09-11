package com.unioncloud.newpay.domain.interactor.appupgrade;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.appupgrade.AppUpgrade;
import com.unioncloud.newpay.domain.repository.AppUpgradeRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/20.
 */
public class GetLastAppInfoInteractor extends BaseInteractor<AppUpgrade> {

    private AppUpgradeRepository repository;
    private String packageName;

    public GetLastAppInfoInteractor(ExecutorProvider provider,
                                    String packageName,
                                    AppUpgradeRepository repository) {
        super(provider);
        this.packageName = packageName;
        this.repository = repository;
    }

    @Override
    protected Observable<AppUpgrade> bindObservable() {
        return repository.getLastAppInfo(packageName);
    }
}
