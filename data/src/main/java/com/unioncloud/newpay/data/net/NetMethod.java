package com.unioncloud.newpay.data.net;

/**
 * Created by cwj on 16/8/8.
 */
public enum NetMethod {
    GET,
    POST,
    PUT,
    DELETE,
    HEAD;

    public String methodName() {
        return this.toString();
    }

    public static NetMethod fromString(String methodName) {
        NetMethod[] values = values();
        for (NetMethod httpMethod : values) {
            if (httpMethod.methodName().equalsIgnoreCase(methodName)) {
                return httpMethod;
            }
        }
        return null;
    }
}
