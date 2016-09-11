package com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass;

/**
 * Created by cwj on 16/8/18.
 */
public class SwiftPassException extends Exception {


    public SwiftPassException() {
    }

    public SwiftPassException(String detailMessage) {
        super(detailMessage);
    }

    public SwiftPassException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SwiftPassException(Throwable throwable) {
        super(throwable);
    }
}
