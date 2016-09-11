package com.unioncloud.newpay.data.repository.appupgrade;

import com.unioncloud.newpay.data.repository.StoreFactory;
import com.unioncloud.newpay.domain.model.appupgrade.AppUpgrade;
import com.unioncloud.newpay.domain.model.appupgrade.DownloadInfo;
import com.unioncloud.newpay.domain.repository.AppUpgradeRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/20.
 */
public class AppDateRepository implements AppUpgradeRepository {

    @Override
    public Observable<AppUpgrade> getLastAppInfo(String packageName) {
        return StoreFactory.getAppStore().getLastAppInfo(packageName);
    }

    @Override
    public Observable<DownloadInfo> downloadApk(String url) {
        return StoreFactory.getAppStore().downloadApk(url);
    }
}
