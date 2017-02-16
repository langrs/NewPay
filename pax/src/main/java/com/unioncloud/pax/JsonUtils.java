package com.unioncloud.pax;

import com.google.gson.Gson;

public class JsonUtils {

    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }

    public static <T> T simpleFromJson(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }
}
