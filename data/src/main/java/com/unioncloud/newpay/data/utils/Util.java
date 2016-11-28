package com.unioncloud.newpay.data.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * utils for parse xml data
 * Created by cwj on 16/8/18.
 */
public class Util {

    public static InputStream getStringStream(String source) {
        ByteArrayInputStream inputStream = null;
        if (source != null && !source.trim().equals("")) {
            inputStream = new ByteArrayInputStream(source.getBytes());
        }
        return inputStream;
    }

    public static String getStringFromMap(Map<String, Object> map, String key, String defaultValue) {
        if (key == "" || key == null) {
            return defaultValue;
        }
        String result = (String) map.get(key);
        if (result == null) {
            return defaultValue;
        } else {
            return result;
        }
    }

    public static int getIntFromMap(Map<String, Object> map, String key) {
        if (key == "" || key == null) {
            return 0;
        }
        if (map.get(key) == null) {
            return 0;
        }
        return Integer.parseInt((String) map.get(key));
    }
}
