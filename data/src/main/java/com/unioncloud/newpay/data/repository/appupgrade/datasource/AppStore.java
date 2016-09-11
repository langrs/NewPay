package com.unioncloud.newpay.data.repository.appupgrade.datasource;

import com.unioncloud.newpay.domain.model.appupgrade.AppUpgrade;
import com.unioncloud.newpay.domain.model.appupgrade.DownloadInfo;

import rx.Observable;

/**
 * Created by cwj on 16/8/20.
 */
public interface AppStore {
    Observable<AppUpgrade> getLastAppInfo(String packageName);

    Observable<DownloadInfo> downloadApk(String url);
}
