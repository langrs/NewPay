package com.unioncloud.newpay.data.net.ssl;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by cwj on 16/7/15.
 */
public class DefaultSSLVerifier implements SSLVerifier {

    private X509TrustManager trustManager;
    private HostnameVerifier hostnameVerifier;

    public DefaultSSLVerifier(KeyStore keyStore) {
        try {
            TrustManagerFactory tmf =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            TrustManager[] tmArray = tmf.getTrustManagers();
            for (TrustManager temp : tmArray) {
                if (temp instanceof X509TrustManager) {
                    trustManager = (X509TrustManager) temp;
                    break;
                }
            }
            hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean verify(String hostname, SSLSession session) {
        if (hostnameVerifier != null) {
            return hostnameVerifier.verify(hostname, session);
        }
        return false;
    }

    protected void checkTrusted(X509Certificate[] chain, String authType) throws CertificateException{
        if (trustManager == null) {
            throw new CertificateException(
                    String.format("No trust manager in %s. Can't trust any certificates.",
                            getClass().getName()));
        }
        trustManager.checkClientTrusted(chain, authType);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        checkTrusted(chain, authType);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        checkTrusted(chain, authType);
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        if (trustManager == null) {
            return new X509Certificate[0];
        }
        return trustManager.getAcceptedIssuers();
    }

}
