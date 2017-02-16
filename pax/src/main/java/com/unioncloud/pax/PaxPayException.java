package com.unioncloud.pax;

import android.support.annotation.StringRes;

public class PaxPayException extends Exception {

    private int messageRes;

    public PaxPayException(String message) {
        super(message);
    }

    public PaxPayException(@StringRes int messageRes) {
        this.messageRes = messageRes;
    }

    @StringRes
    public int getMessageRes() {
        return messageRes;
    }
}
