package com.unioncloud.newpay.data.repository.appupgrade.datasource;

import android.os.Environment;

import com.google.gson.JsonSyntaxException;
import com.unioncloud.newpay.data.exception.RemoteDataException;
import com.unioncloud.newpay.data.repository.WebServiceUrlConst;
import com.unioncloud.newpay.data.utils.JsonUtils;
import com.unioncloud.newpay.domain.model.appupgrade.AppUpgrade;
import com.unioncloud.newpay.domain.model.appupgrade.DownloadInfo;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * Created by cwj on 16/8/20.
 */
public class CloudAppStore implements AppStore {

    @Override
    public Observable<AppUpgrade> getLastAppInfo(String packageName) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                RequestParams params = new RequestParams(WebServiceUrlConst.getAppUpgrade());
//              params.setSslSocketFactory(sslContext.getSocketFactory());
//                params.setConnectTimeout(SwiftPassConst.TIMEOUT);
                params.setMaxRetryCount(0);
                params.setCharset("UTF-8");
                try {
                    String result = x.http().postSync(params, String.class);
                    if (result != null && !"".equals(result)) {
                        subscriber.onNext(result);
                    } else {
                        subscriber.onError(new RemoteDataException("获取最新App版本失败"));
                    }
                } catch (Throwable throwable) {
                    subscriber.onError(new RemoteDataException("获取最新App版本失败"));
                }
            }
        }).map(new Func1<String, AppUpgrade>() {
            @Override
            public AppUpgrade call(String s) {
                try {
                    AppUpgrade appUpgrade = JsonUtils.fromJson(s, AppUpgrade.class);
                    if (appUpgrade != null) {
                        return appUpgrade;
                    } else {
                        throw Exceptions.propagate(new RemoteDataException("获取最新App版本错误"));
                    }
                } catch (JsonSyntaxException e) {
                    throw Exceptions.propagate(new RemoteDataException("获取最新App版本错误"));
                }
            }
        });
    }

    @Override
    public Observable<DownloadInfo> downloadApk(final String url) {
        return Observable.create(new Observable.OnSubscribe<DownloadInfo>() {
            @Override
            public void call(Subscriber<? super DownloadInfo> subscriber) {
                RequestParams params = new RequestParams(url);
//              params.setSslSocketFactory(sslContext.getSocketFactory());
//                params.setConnectTimeout(SwiftPassConst.TIMEOUT);
//                params.setMaxRetryCount(0);
//                params.setCharset("UTF-8");
                params.setSaveFilePath(getDownloadCachePath());
                try {
//                    x.http().requestSync(HttpMethod.GET, params, new DownloadCallback(subscriber));
                    x.http().get(params, new DownloadCallback(subscriber));
                } catch (Throwable throwable) {
                    subscriber.onError(new RemoteDataException("下载失败"));
                }
            }
        });
    }

    private String getDownloadCachePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()+"/HS_update.apk";
    }

    private static class DownloadCallback implements Callback.CommonCallback<File>, Callback.ProgressCallback<File> {

        Subscriber<? super DownloadInfo> subscriber;
        DownloadInfo info;

        public DownloadCallback(Subscriber<? super DownloadInfo> subscriber) {
            this.subscriber = subscriber;
            info = new DownloadInfo();
        }

//        @Override
//        public Type getLoadType() {
//            return File.class;
//        }

        @Override
        public void onWaiting() {
        }

        @Override
        public void onStarted() {
        }

        @Override
        public void onLoading(long total, long current, boolean isDownloading) {
            info.setCurrent(current);
            info.setTotal(total);
            subscriber.onNext(info);
        }

        @Override
        public void onSuccess(File result) {
            info.setPath(result);
            subscriber.onNext(info);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
        }

        @Override
        public void onCancelled(CancelledException cex) {
        }

        @Override
        public void onFinished() {
        }
    }
}
