package com.unioncloud.newpay.domain.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by cwj on 16/7/1.
 */
public class DateFormatUtils {

    public static final String DATE_FORMATE_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMATE_2 = "yyyy-MM-dd";
    public static final String DATE_FORMATE_3 = "yyyyMMddHHmmss";
    public static final String DATE_FORMATE_4 = "yyyyMMdd";

    private static final ThreadLocal<SimpleDateFormat> threadLocal1 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_FORMATE_1, Locale.getDefault());
        }
    };
    private static final ThreadLocal<SimpleDateFormat> threadLocal2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_FORMATE_2, Locale.getDefault());
        }
    };
    private static final ThreadLocal<SimpleDateFormat> threadLocal3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_FORMATE_3, Locale.getDefault());
        }
    };
    private static final ThreadLocal<SimpleDateFormat> threadLocal4 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_FORMATE_4, Locale.getDefault());
        }
    };

    public static String yyyyMMdd(Date date) {
        return threadLocal4.get().format(date);
    }

    public static String yyyy_MM_dd(Date date) {
        return threadLocal2.get().format(date);
    }

    public static String yyyyMMddHHmmss(Date date) {
        return threadLocal3.get().format(date);
    }

    public static String allFriendlyFormat(Date date) {
        return threadLocal1.get().format(date);
    }

    public static Date parseYYYY_MM_DD(String dateStr) {
        try {
            return threadLocal2.get().parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

}
