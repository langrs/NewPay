package com.esummer.android;

//import android.support.multidex.MultiDex;
import android.app.Application;
import android.content.Context;

import com.raizlabs.coreutils.logging.Logger;

/**
 * Created by cwj on 16/7/11.
 */
public class AppContext extends Application {

    private static AppContext instance = null;

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public static AppContext getApplication() {
        return instance;
    }

    public void attachBaseContext(Context paramContext) {
        super.attachBaseContext(paramContext);
//        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Logger.setLogLevel(Logger.LogLevel.ALL);
    }
}
