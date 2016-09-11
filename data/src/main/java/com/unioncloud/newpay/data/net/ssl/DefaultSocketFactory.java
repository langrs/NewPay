package com.unioncloud.newpay.data.net.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

/**
 * Created by cwj on 16/7/18.
 */
public class DefaultSocketFactory extends SocketFactory {
    private SSLVerifier verifier;
    private SSLContext sslContext;
    private static final int CONNECT_TIMEOUT = 30 * 1000;
    private static final int SO_TIMEOUT = 30 * 1000;

    public DefaultSocketFactory(SSLVerifier verifier) {
        this.verifier = verifier;
    }

    public SSLContext getSslContext() {
        if (sslContext == null) {
            sslContext = SSLUtils.createContext(verifier);
        }
        return sslContext;
    }

    public SSLVerifier getVerifier() {
        return verifier;
    }

    public void resetVerifier(SSLVerifier verifier) {
        this.verifier = verifier;
        this.sslContext = null;
    }

    protected boolean verify(String hostname, SSLSocket sslSocket) {
        if (verifier != null) {
            return verifier.verify(hostname, sslSocket.getSession());
        }
        return true;
    }

    @Override
    public Socket createSocket() throws IOException {
        return getSslContext().getSocketFactory().createSocket();
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return createSocket(host, port, null, 0);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        InetSocketAddress remoteAddress  = new InetSocketAddress(host, port);
        Socket sock = createSocket();
        if (sock instanceof SSLSocket) {
            SSLSocket sslSocket = (SSLSocket) sock;
            if (!verify(host, sslSocket)) {
                throw new IOException("Server was not trusted!");
            }
        }
        // 这里判断localAddress != null, 因为如果有地址指定表示有bind()本地地址的意愿.
        // 而不是防止抛出NullPointerException
        // 无论是new InetSocketAddress, 还说bind()都对localAddress == null做过处理(最终绑定的是任意可用地址)
        if ((localHost != null) || localPort > 0) {
            if (localPort < 0) {
                localPort = 0;
            }
            sock.bind(new InetSocketAddress(localHost, localPort));
        }
        sock.connect(remoteAddress, CONNECT_TIMEOUT);
        sock.setSoTimeout(SO_TIMEOUT);
        return sock;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return null;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return null;
    }
}
