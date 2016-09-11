package com.unioncloud.newpay.presentation.presenter.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.unioncloud.newpay.domain.utils.DateFormatUtils;

import java.util.Date;
import java.util.Locale;

/**
 * Created by cwj on 16/8/11.
 */
public class PreferencesUtils {

    private static final String PREFERENCES_NAME = "NEW_PAY";

    public static String getOrderSerialNumber(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String lastOrderId = sp.getString("LastOrderTag", null);
        if (lastOrderId != null) {
            String serialNumberDate = DateFormatUtils.yyyyMMdd(new Date());
            if (lastOrderId.startsWith(serialNumberDate)) {
                int lastSerialNumber = Integer.valueOf(
                        lastOrderId.substring(serialNumberDate.length()));
                if (lastSerialNumber == 9999) {     // 序列号最大不超过4位
                    return "0001";
                }
                lastSerialNumber += 1;
                String lastSerialNumberStr = String.format(Locale.getDefault(), "%04d", lastSerialNumber);
                return lastSerialNumberStr;
            }
        }
        return "0001";
    }

    public static String skipOrderSerialNumber(Context context) {
        String lastSerialNumberStr = getOrderSerialNumber(context);
        String serialNumberDate = DateFormatUtils.yyyyMMdd(new Date());
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        sp.edit().putString("LastOrderTag", serialNumberDate + lastSerialNumberStr).apply();
        int lastSerialNumber = Integer.valueOf(lastSerialNumberStr);
        lastSerialNumber += 1;
        if (lastSerialNumber == 9999) {     // 序列号最大不超过4位
            lastSerialNumber = 1;
        }
        lastSerialNumberStr = String.format(Locale.getDefault(), "%04d", lastSerialNumber);
        return lastSerialNumberStr;
    }

    public static boolean saveLastOrderSerialNumber(Context context, String lastOrderSerialNumber) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String serialNumberDate = DateFormatUtils.yyyyMMdd(new Date());
        return sp.edit().putString("LastOrderTag", serialNumberDate + lastOrderSerialNumber).commit();
    }


    public static int getPrintCount(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getInt("PrintCount", 2);
    }

    public static boolean setPrintCount(Context context, int count) {
        if (count < 1) {
            count = 1;
        }
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.edit().putInt("PrintCount", count).commit();
    }

    public static String getLastUser(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.getString("lastUser", null);
    }

    public static boolean saveLastUser(Context context, String lastUser) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp.edit().putString("lastUser", lastUser).commit();
    }

    public static void coachCount(Context context) {
        int coachCount = 0;
        if(context != null) {
            SharedPreferences sp = context.getSharedPreferences("COACH_CELL_PREFERENCES", 0);
            if(sp != null) {
                coachCount = sp.getInt("COACH_CELL_ANIMATION_COUNT", 0);
            }
            SharedPreferences.Editor edit = sp.edit();
            edit.putInt("COACH_CELL_ANIMATION_COUNT", coachCount + 1);
            edit.apply();
        }
    }
}
