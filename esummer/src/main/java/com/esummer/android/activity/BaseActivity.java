package com.esummer.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.esummer.android.FragmentManagerProvider;
import com.esummer.android.FragmentStackDelegate;
import com.esummer.android.appupgrade.AppUpgrade;
import com.esummer.android.appupgrade.AppUpgradeDialog;
import com.esummer.android.appupgrade.AppUpgradeHelper;
import com.esummer.android.R;
import com.esummer.android.appupgrade.AppUpgradeListener;
import com.esummer.android.dialog.DefaultDialogBuilder;
import com.esummer.android.dialog.DialogListenerProvider;
import com.esummer.android.dialog.UniversalDialog;
import com.esummer.android.fragment.StateManagerProvider;
import com.esummer.android.net.connectivity.ConnectionUtil;
import com.esummer.android.task.UITaskQueue;
import com.esummer.android.fragment.StateManager;
import com.esummer.android.utils.CommonUtils;
import com.raizlabs.coreutils.threading.ThreadingUtils;

/**
 * Created by cwj on 16/7/6.
 */
public abstract class BaseActivity extends AppCompatActivity
        implements StateManagerProvider, FragmentStackDelegate, FragmentManagerProvider, DialogListenerProvider {
    private static final int PROGRESS_DIALOG_ID = UniversalDialog.getAutoId();

    private UITaskQueue uiTaskQueue;
    private ConnectionDialogUtils connectionDialogUtils;
    private AppUpgradeDialog appUpgradeDialog;
    private boolean needAppUpgrade;

    protected boolean isHome = false;
    //    private DrawerHelper drawerHelper;
//    protected DefaultDialogBuilder progressDialogBuilder;
    protected StateManager stateManager;


    private void onBackTransition() {
        switch (getTransitionMode()) {
            case 1:
                CommonUtils.pendingFadeDown(this);
                break;
            case 2:
                CommonUtils.pendingFadeRight(this);
                break;
            case 3:
                CommonUtils.pendingDefault(this);
                break;
        }
    }

    private void onCreateTransition() {
        switch (getTransitionMode()) {
            case 1:
                CommonUtils.pendingUpBack(this);
                break;
            case 2:
                CommonUtils.pendingLeftBack(this);
                break;
            case 3:
                CommonUtils.pendingDefault(this);
                break;
        }
    }

    protected int getTransitionMode() {
        return 0;
    }

    protected void setToolbar(int toolbarId) {
        Toolbar toolbar = (Toolbar) findViewById(toolbarId);
        if (toolbar != null) {
            setToolbar(toolbar);
            toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable
                    .abc_ic_ab_back_mtrl_am_alpha);
        }
    }

    protected void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }

    protected Toolbar findToolbar(int toolbarId) {
        return (Toolbar) findViewById(toolbarId);
    }

    public void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    public void setTitle(int titleId) {
        this.setTitle(getString(titleId));
    }

    @Override
    public void setTitle(final CharSequence title) {
        ThreadingUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                BaseActivity.super.setTitle(title);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiTaskQueue = new UITaskQueue();
        onCreateTransition();
        if (getIntent() == null) {
            setIntent(new Intent());
        }
        if (getIntent().getExtras() == null) {
            getIntent().putExtra("", "");
        }
        stateManager = new StateManager(this);
        stateManager.initStacks(savedInstanceState);

//        progressDialogBuilder = new DefaultDialogBuilder(PROGRESS_DIALOG_ID)
//                .setMessage(getString(R.string.dialog_in_progress_general_message))
//                .setStyle(UniversalDialog.STYLE_PROGRESSBAR)
//                .setCancelable(false);
//        getUIStateManager().bindActionListener(progressDialogBuilder);

        connectionDialogUtils = new ConnectionDialogUtils(this, getUIStateManager());
        connectionDialogUtils.init(getResources());
        setTitle(getTitle());
    }

    @Override
    protected void onDestroy() {
        stateManager.onDestroy();
        stateManager = null;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (connectionDialogUtils != null) {
            connectionDialogUtils.removeListener();
        }
        if (appUpgradeDialog != null) {     // 如果提示更新app的dialog不为null
            needAppUpgrade = true;        // 设置需要更新
            // 设置 updateData为静态的全局变量
            AppUpgradeHelper.getInstance().appUpgrade = appUpgradeDialog.getAppUpgrade();
            appUpgradeDialog.dismiss();     // 隐藏更新对话框
        }
        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        l().d();    // DrawerHelper.syncState()
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        uiTaskQueue.loop();
        if (needAppUpgrade) {  // 判断是否需要更新
            AppUpgrade appUpgrade = AppUpgradeHelper.getInstance().appUpgrade;
            if (appUpgrade != null) {
                showAppUpgradeDialog(appUpgrade);
                AppUpgradeHelper.getInstance().appUpgrade = null;
            } else {
                if (ConnectionUtil.isConnectedOrConnecting(getApplicationContext())) {
                    AppUpgradeHelper.getInstance().loadAppUpgrade(new AppUpgradeListener() {
                        @Override
                        public void onUpgrade(AppUpgrade appUpgrade) {
                            showAppUpgradeDialog(appUpgrade);
                        }
                    });
                }
            }
        }
    }

    protected void showAppUpgradeDialog(AppUpgrade appUpgrade) {
        appUpgradeDialog = AppUpgradeDialog.newInstance(appUpgrade);
        appUpgradeDialog.show(getSupportFragmentManager(), "AppUpgradeFragment");
    }

    @Override
    protected void onResume() {
        /* CartDataManager.updateOnAppStart(Context).因为该应用是不取网络中的购物车,所有不用该方法  */
//        com.bhphoto.d.m.g().b().updateOnAppStart(this);
        if (connectionDialogUtils != null) {
            connectionDialogUtils.registerListener();
        }
        super.onResume();
    }

//    protected void a(Runnable paramRunnable)
    protected void runOnUIThread(Runnable action) {
        if (uiTaskQueue != null) {
            uiTaskQueue.execute(action);
        }
    }

//    public l m()
    public StateManager getUIStateManager() {
        return this.stateManager;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackTransition();
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        uiTaskQueue.pause();
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public StateManager getStateManager() {
        return this.stateManager;
    }

    @Override
    public FragmentManager providerFM() {
        return getSupportFragmentManager();
    }

    @Override
    public UniversalDialog.ActionListener provide() {
        return stateManager;
    }
}
