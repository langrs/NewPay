package com.esummer.android.appupgrade;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esummer.android.fragment.StatedFragment;

/**
 * Created by cwj on 16/7/13.
 */
public class AppUpgradeDialog extends StatedFragment {
    private AppUpgrade appUpgrade;

    public static AppUpgradeDialog newInstance(AppUpgrade appUpgrade) {
        AppUpgradeDialog dialog = new AppUpgradeDialog();
        dialog.appUpgrade = appUpgrade;
        return dialog;
    }

    public AppUpgrade getAppUpgrade() {
        return appUpgrade;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO 初始化
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        if (this.appUpgrade != null) {
            String message = appUpgrade.message;
            if (!TextUtils.isEmpty(message)) {
                // TODO 设置更新的提示信息
            }
            String version = appUpgrade.appVersion;
            if (!TextUtils.isEmpty(version)) {
                // TODO 设置更新的APP版本
            }
        }
    }

    private void confirmUpgrade() {
        // TODO 确认更新
    }

    private void cancelUpgrade() {
        saveAppUpgrade(getActivity());
        // 设置Activity中对该dialog的引用为null
        getDialog().dismiss();
    }

    // 这个方法应该抽离到全局SharedPreferences的静态方法中
    private void saveAppUpgrade(Context context) {
        long time = System.currentTimeMillis();
        SharedPreferences.Editor edit = context.getSharedPreferences("AppUpgradePrefsFile", 0).edit();
        edit.putLong("Date", time);
        edit.apply();
    }

}
