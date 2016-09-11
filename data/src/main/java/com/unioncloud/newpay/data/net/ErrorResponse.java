package com.unioncloud.newpay.data.net;

import com.raizlabs.coreutils.listeners.ProgressListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by cwj on 16/8/8.
 */
public class ErrorResponse implements NetResponse {

    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    @Override
    public String getStringData() {
        return null;
    }

    @Override
    public boolean getFileData(File target, ProgressListener listener) {
        return false;
    }

    @Override
    public int getResponseCode() throws IOException {
        return 0;
    }

    @Override
    public String getResponseMessage() throws IOException {
        return message;
    }

    @Override
    public long getContentLength() {
        return 0;
    }

    @Override
    public InputStream getInputStreamData() throws IOException {
        return null;
    }

    @Override
    public void disconnect() {

    }
}
