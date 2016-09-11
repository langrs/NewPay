package com.esummer.android.transport;

//import com.esummer.android.net.response.Response;

/**
 * Created by cwj on 16/7/5.
 */
public class ResponseStateUtils {

    public static boolean isSuccess(int code) {
        return code / 100 == 2;
    }

//    public static boolean isSuccess(Response response) {
//        return isSuccess(response.getResponseCode());
//    }
}
