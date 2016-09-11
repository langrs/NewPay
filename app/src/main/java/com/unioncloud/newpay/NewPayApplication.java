package com.unioncloud.newpay;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import org.xutils.x;

/**
 * Created by cwj on 16/7/22.
 */
public class NewPayApplication extends MultiDexApplication {
    private static NewPayApplication instance;

    public static NewPayApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        x.Ext.init(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
