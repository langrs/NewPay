package com.unioncloud.newpay.presentation.ui.welcome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.esummer.android.dialog.DefaultDialog;
import com.esummer.android.dialog.DefaultDialogBuilder;
import com.esummer.android.dialog.UniversalDialog;
import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.esummer.android.utils.CommonUtils;
import com.raizlabs.coreutils.listeners.ProgressListener;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.appupgrade.AppUpgrade;
import com.unioncloud.newpay.presentation.presenter.appupgrade.DownloadAppHandler;
import com.unioncloud.newpay.presentation.presenter.appupgrade.GetLastAppHandler;
import com.unioncloud.newpay.presentation.ui.login.LoginActivity;

import java.io.File;
import java.io.IOException;

/**
 * Created by cwj on 16/8/20.
 */
public class WelcomeFragment extends StatedFragment implements ProgressListener {

    private static StateUpdateHandlerListener<WelcomeFragment, GetLastAppHandler> lastAppHandlerListener =
            new StateUpdateHandlerListener<WelcomeFragment, GetLastAppHandler>() {
                @Override
                public void onUpdate(String key, WelcomeFragment handler, GetLastAppHandler response) {
                    handler.dealLastVersion(response);
                }

                @Override
                public void onCleanup(String key, WelcomeFragment handler, GetLastAppHandler response) {
                    response.removeCompletionListener(handler.lastAppListener);
                }
            };
    private UpdateCompleteCallback<GetLastAppHandler> lastAppListener =
            new UpdateCompleteCallback<GetLastAppHandler>() {
                @Override
                public void onCompleted(GetLastAppHandler response, boolean isSuccess) {
                    dealLastVersion(response);
                }
            };


    private static StateUpdateHandlerListener<WelcomeFragment, DownloadAppHandler> downloadAppHandlerListener =
            new StateUpdateHandlerListener<WelcomeFragment, DownloadAppHandler>() {
                @Override
                public void onUpdate(String key, WelcomeFragment handler, DownloadAppHandler response) {
                    handler.dealDownload(response);
                }

                @Override
                public void onCleanup(String key, WelcomeFragment handler, DownloadAppHandler response) {
                    response.removeCompletionListener(handler.downloadListener);
                }
            };
    private UpdateCompleteCallback<DownloadAppHandler> downloadListener =
            new UpdateCompleteCallback<DownloadAppHandler>() {
                @Override
                public void onCompleted(DownloadAppHandler response, boolean isSuccess) {
                    dealDownload(response);
                }
            };


    public static WelcomeFragment newInstance() {
        return new WelcomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AlphaAnimation animation = new AlphaAnimation(0.3f,1.0f);
        animation.setDuration(3000);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation ani) {
                if(isUpdating()) {
                    putFinishAnimation();
                } else {
                    enterLogin();
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationStart(Animation animation) {}

        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLastVersion();
    }

    private boolean isUpdating() {
        return getArguments().getBoolean("appupgrade");
    }

    private boolean isFinishAnimation() {
        return getArguments().getBoolean("FinishAnimation");
    }

    private void putFinishAnimation() {
        getArguments().putBoolean("FinishAnimation", true);
    }

    private void setUpdating() {
        getArguments().putBoolean("appupgrade", true);
    }

    private void removeUpdating() {
        getArguments().remove("appupgrade");
    }

    private void getLastVersion() {
        setUpdating();
        String packageName = getContext().getPackageName();
        GetLastAppHandler handler = new GetLastAppHandler(packageName);
        updateForResponse("WelcomeFragment:getLastVersion", handler, lastAppHandlerListener);
        handler.run();
    }

    private void dealLastVersion(GetLastAppHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("版本检测...");
                handler.addCompletionListener(lastAppListener);
            } else {
                if (handler.isSuccess()) {
                    checkVersion(handler.getData());
                } else {
                    showToast("无法获取到最新版本信息");
                    finishUpdating();
                }
                cleanupResponse("WelcomeFragment:getLastVersion");
                dismissProgressDialog();
            }
        }
    }

    private static final int DIALOG_DOWNLOADING_APP = 0x1248;

    private void finishUpdating() {
        removeUpdating();
        if (isFinishAnimation()) {
            enterLogin();
        }
    }

    private void checkVersion(AppUpgrade appUpgrade) {
        String localVersion = CommonUtils.getCurrentVersion(getContext());
        if (appUpgrade.getVersion().compareTo(localVersion) <= 0) {
            finishUpdating();
            return;
        }
        DefaultDialogBuilder builder = createAndSaveDialogBuilder(DIALOG_DOWNLOADING_APP);
        builder.setStyle(UniversalDialog.STYLE_PROGRESSBAR);
        builder.setTitle(null);
        builder.setMessage("正在下载新版本");
        showDialog(builder);
        DownloadAppHandler handler = new DownloadAppHandler(appUpgrade.getUrl(), this);
        updateForResponse("WelcomeFragment:download", handler, downloadAppHandlerListener);
        handler.run();
    }

    private void dealDownload(DownloadAppHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                handler.addCompletionListener(downloadListener);
            } else {
                if (handler.isSuccess()) {
                    showInstallApp(handler.getData().getPath());
                }
                cleanupResponse("WelcomeFragment:download");
                dismissDialog(getSavedBuilder(DIALOG_DOWNLOADING_APP));
            }
        }
    }

    public void showInstallApp(File appfile) {
        if(appfile.exists()) {
            String[] command = { "chmod", "777", appfile.getPath() };
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(appfile), "application/vnd.android.package-archive");
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onProgressUpdate(long currentProgress, long maxProgress) {
        DefaultDialog dialog = (DefaultDialog) getSavedDialog(DIALOG_DOWNLOADING_APP);
        if (dialog != null) {
            int progress = (int) (currentProgress * 100 / maxProgress);
            dialog.setMessage("正在下载新版本: " + progress + "%");
        }
    }

    private void enterLogin() {
        Intent intent = LoginActivity.getLoginIntent(getActivity());
        startActivity(intent);
        getActivity().finish();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
