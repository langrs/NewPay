package com.unioncloud.newpay.data.exception;

/**
 * Created by cwj on 16/8/12.
 */
public class RemoteDataException extends Exception {

    public RemoteDataException() {
    }

    public RemoteDataException(String detailMessage) {
        super(detailMessage);
    }

    public RemoteDataException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RemoteDataException(Throwable throwable) {
        super(throwable);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
