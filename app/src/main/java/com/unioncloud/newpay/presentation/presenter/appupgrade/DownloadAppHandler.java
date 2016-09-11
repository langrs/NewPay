package com.unioncloud.newpay.presentation.presenter.appupgrade;

import com.esummer.android.updatehandler.UpdateHandler;
import com.raizlabs.coreutils.listeners.ProgressListener;
import com.unioncloud.newpay.domain.interactor.appupgrade.DownloadAppInteractor;
import com.unioncloud.newpay.domain.model.appupgrade.DownloadInfo;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/20.
 */
public class DownloadAppHandler extends UpdateHandler<DownloadInfo, DownloadAppHandler>
        implements Runnable {

    private String url;
    private ProgressListener progressListener;

    public DownloadAppHandler(String url, ProgressListener progressListener) {
        super(null, true);
        this.url = url;
        this.progressListener = progressListener;
    }

    @Override
    public void run() {
        DownloadAppInteractor interactor = new DownloadAppInteractor(
                PresenterUtils.getExecutorProvider(),
                url,
                PresenterUtils.getAppUpgradeRepository());
        interactor.execute(new Subscriber<DownloadInfo>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(DownloadInfo downloadInfo) {
                data = downloadInfo;
                if (downloadInfo.getPath() != null) {
                    onUpdateCompleted();
                } else {
                    progressListener.onProgressUpdate(downloadInfo.getCurrent(),
                            downloadInfo.getTotal());
                }
            }
        });
    }
}
