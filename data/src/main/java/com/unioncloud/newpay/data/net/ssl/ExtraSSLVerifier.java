package com.unioncloud.newpay.data.net.ssl;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLSession;


/**
 * Created by cwj on 16/7/15.
 */
public interface ExtraSSLVerifier {

    /**
     * Verifies that the specified hostname is allowed within the specified SSL
     * session.
     * @param urlHostname
     * @param sslContext
     * @return
     */
    boolean verify(String urlHostname, SSLSession sslContext);

    /**
     * Checks whether the specified certificate chain (partial or complete) can
     * be validated and is trusted
     * @param chain
     * @param authType  the authentication type used.
     * @param isServer  true if server authentication
     * @return
     */
    boolean isTrusted(X509Certificate[] chain, String authType, boolean isServer);
}
