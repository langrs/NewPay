package com.unioncloud.newpay.data.net.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.X509TrustManager;

/**
 * Created on 16/6/23.
 */
public interface SSLVerifier extends HostnameVerifier, X509TrustManager {

}
