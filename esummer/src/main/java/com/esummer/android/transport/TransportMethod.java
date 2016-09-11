package com.esummer.android.transport;

/**
 * Created by cwj on 16/7/15.
 */
public enum TransportMethod {
    GET,
    POST,
    PUT,
    DELETE,
    HEAD;

    public String methodName() {
        return this.toString();
    }

    public static TransportMethod fromString(String methodName) {
        TransportMethod[] values = values();
        for (TransportMethod httpMethod : values) {
            if (httpMethod.methodName().equalsIgnoreCase(methodName)) {
                return httpMethod;
            }
        }
        return null;
    }
}
