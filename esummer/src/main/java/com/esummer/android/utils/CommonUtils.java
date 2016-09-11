package com.esummer.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.esummer.android.R;

/**
 * Created by cwj on 16/7/11.
 */
public class CommonUtils {

    public static String getCurrentVersion(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pInfo = pm.getPackageInfo(context.getPackageName(), 0);
//			return Double.valueOf(pInfo.versionName);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1";
    }

    public static boolean checkSelfPermission(Context context, String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, permission);
    }

    public static void pendingFadeDown(Activity activity) {
        if (activity != null) {
            activity.overridePendingTransition(R.anim.fade_up_in, R.anim.slide_down);
        }
    }

    public static void pendingFadeRight(Activity activity) {
        if (activity != null) {
            activity.overridePendingTransition(R.anim.fade_up_in, R.anim.slide_right);
        }
    }

    public static void pendingDefault(Activity activity) {
        if (activity != null) {
            activity.overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
        }
    }

    public static void pendingUpBack(Activity activity) {
        if (activity != null) {
            activity.overridePendingTransition(R.anim.slide_up, R.anim.fade_back_out);
        }
    }

    public static void pendingLeftBack(Activity activity) {
        if (activity != null) {
            activity.overridePendingTransition(R.anim.slide_left, R.anim.fade_back_out);
        }
    }


}
