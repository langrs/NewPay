package com.esummer.android.appupgrade;


/**
 * Created by cwj on 16/7/13.
 */
public class AppUpgradeHelper {

    private static AppUpgradeHelper instance = new AppUpgradeHelper();
    public AppUpgrade appUpgrade;

    public static AppUpgradeHelper getInstance() {
        if (instance == null) {
            instance = new AppUpgradeHelper();
        }
        return instance;
    }

    public void loadAppUpgrade(AppUpgradeListener listener) {
//        NetTransportManager transportManager = null; // 通过静态工具类来初始化
        // TODO Í网络请求
    }
}
