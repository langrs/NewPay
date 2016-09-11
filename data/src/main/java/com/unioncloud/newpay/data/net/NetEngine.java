package com.unioncloud.newpay.data.net;

import java.io.IOException;

import javax.net.SocketFactory;

/**
 * Created by cwj on 16/8/8.
 */
public interface NetEngine {
    /**
     * Set the net connections max number.
     * @param maxTotal
     */
    void setMaxConnections(int maxTotal);

    /**
     * Set the maximum time in milliseconds to wait while connecting.
     * @param timeout Timeout in milliseconds.
     */
    void setConnectTimeout(int timeout);

    /**
     * Set the default socket timeout in milliseconds which is the
     * timeout for waiting for data.
     * @param timeout Timeout in milliseconds.
     */
    void setSoTimeout(int timeout);

    /**
     * Set the SSL SocketFactory
     * @param socketFactory
     */
    void setSSLSocketFactory(SocketFactory socketFactory);

    NetResponse executeRequest(NetRequest request) throws IOException;
}
