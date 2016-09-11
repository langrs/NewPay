package com.esummer.android.transport.ssl;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * Created by cwj on 16/7/15.
 */
public class SSLVerifierDelegate implements SSLVerifier {

    private X509TrustManager trustManager;
    private HostnameVerifier hostnameVerifier;
    private ExtraSSLVerifier extraSSLVerifier;

    public SSLVerifierDelegate(KeyStore keyStore, ExtraSSLVerifier verifier) {
        this.extraSSLVerifier = verifier;
    }

    public SSLVerifierDelegate(X509TrustManager trustManager, ExtraSSLVerifier verifier) {
        setTrustManager(trustManager);
        setHostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier());
        setExtraSSLVerifier(verifier);
    }

    public void setTrustManager(X509TrustManager trustManager) {
        this.trustManager = trustManager;
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
    }

    public void setExtraSSLVerifier(ExtraSSLVerifier extraSSLVerifier) {
        this.extraSSLVerifier = extraSSLVerifier;
    }

    private boolean extraCheckTrusted(X509Certificate[] chain, String authType, boolean isServer) {
        if (this.extraSSLVerifier != null) {
            return this.extraSSLVerifier.isTrusted(chain, authType, isServer);
        }
        return false;
    }

    @Override
    public boolean verify(String hostname, SSLSession session) {
        if (extraSSLVerifier != null) {
            return extraSSLVerifier.verify(hostname, session);
        }
        return false;
    }

    protected void checkTrusted(X509Certificate[] chain, String authType, boolean isServer)
            throws CertificateException {
        if (this.trustManager != null) {
            try {
                if (isServer) {
                    trustManager.checkServerTrusted(chain, authType);
                } else {
                    trustManager.checkClientTrusted(chain, authType);
                }
            } catch (CertificateException e) {
                if (!extraCheckTrusted(chain, authType, isServer)) {
                    throw new CertificateException("Delegate didn't trust the certificate and no default manager found!");
                }
            }
        }
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        checkTrusted(chain, authType, false);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        checkTrusted(chain, authType, true);
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        if (trustManager != null) {
            return trustManager.getAcceptedIssuers();
        }
        return null;
    }

}
