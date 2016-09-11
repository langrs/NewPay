package com.esummer.android.transport.ssl;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;

/**
 * Created by cwj on 16/7/15.
 */
public class SSLUtils {
    public static SSLVerifier getVerifier(ExtraSSLVerifier adapter,
                                          KeyStore keyStore) {
        KeyStore ks = keyStore;
        if (keyStore == null) {
            ks = defaultKeyStore();
        }
        return new SSLVerifierDelegate(ks, adapter);
    }

    public static SSLVerifier getVerifier(KeyStore keyStore) {
        KeyStore ks = keyStore;
        if (keyStore == null) {
            ks = defaultKeyStore();
        }
        return new DefaultSSLVerifier(ks);
    }

    public static KeyStore defaultKeyStore() {
        return null;
    }

    public static SSLContext createContext(SSLVerifier verifier) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            SecureRandom secureRandom = new SecureRandom();
            sslContext.init(null, new SSLVerifier[] { verifier }, secureRandom);
            return sslContext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }
}
