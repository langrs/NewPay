package com.unioncloud.newpay.data.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by cwj on 16/8/12.
 */
public class JsonUtils {


    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static <T> T fromJson(String json, Type clazz) {
        return new Gson().fromJson(json, clazz);
    }
}
