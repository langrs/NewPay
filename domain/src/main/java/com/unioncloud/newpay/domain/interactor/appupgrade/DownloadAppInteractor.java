package com.unioncloud.newpay.domain.interactor.appupgrade;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.appupgrade.DownloadInfo;
import com.unioncloud.newpay.domain.repository.AppUpgradeRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/20.
 */
public class DownloadAppInteractor extends BaseInteractor<DownloadInfo> {

    private String url;
    private AppUpgradeRepository repository;

    public DownloadAppInteractor(ExecutorProvider provider,
                                 String url,
                                 AppUpgradeRepository repository) {
        super(provider);
        this.url = url;
        this.repository = repository;
    }

    @Override
    protected Observable<DownloadInfo> bindObservable() {
        return repository.downloadApk(url);
    }
}
