package com.esummer.android.transport;

import com.raizlabs.coreutils.listeners.ProgressListener;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by cwj on 16/7/15.
 */
public interface TransportResponse {
    /**
     * Returns the response data as string
     * @return
     */
    String getStringData();

    /**
     * Returns the response data as File.
     * @param target  using to save the data
     * @param listener listen what is progress on saving
     * @return {@code true} if succeed,
     *         {@code false} if file not found or other
     */
    boolean getFileData(File target, ProgressListener listener);

    /**
     * Returns the response data as JSONObject
     * @return
     */
    JSONObject getJsonData();

    /**
     * Returns the status code of response
     * @return
     */
    int getResponseCode() throws IOException;

    /**
     * Returns the status message of response
     * @return
     */
    String getResponseMessage() throws IOException;

    /**
     * Returns the content length in bytes specified by the response
     * @return
     */
    long getContentLength();

    /**
     * Return an {@code InputStream} for reading data from the response
     * @return
     */
    InputStream getInputStreamData() throws IOException;

    void disconnect();
}
