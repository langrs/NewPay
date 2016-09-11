package com.unioncloud.newpay.data.net.webservice;

import com.unioncloud.newpay.data.net.ErrorResponse;
import com.unioncloud.newpay.data.net.NetEngine;
import com.unioncloud.newpay.data.net.NetRequest;
import com.unioncloud.newpay.data.net.NetResponse;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.net.SocketFactory;

/**
 * Created by cwj on 16/8/8.
 */
public class WebServiceEngine implements NetEngine {

    private static WebServiceEngine instance;

    private ArrayList<HeaderProperty> headerProperties = new ArrayList<>();

    private int maxToal;
    private int connectTimeout;
    private int soTimeout;



    private SocketFactory socketFactory;

    public static WebServiceEngine getInstance() {
        if (instance == null) {
            instance = new WebServiceEngine();
        }
        return instance;
    }

    private WebServiceEngine() {
        headerProperties.add(new HeaderProperty("Connection", "close"));
    }

    @Override
    public void setMaxConnections(int maxTotal) {
        this.maxToal = (maxTotal > 10) ? 10 : maxTotal;
    }

    @Override
    public void setConnectTimeout(int timeout) {
        this.connectTimeout = timeout;
    }

    @Override
    public void setSoTimeout(int timeout) {
        this.soTimeout = timeout;
    }

    @Override
    public void setSSLSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    @Override
    public NetResponse executeRequest(NetRequest request) throws IOException {

        SoapObject soapObject = request.getSoapRequest();
        SoapSerializationEnvelope envelope = createEnvelope(soapObject);
        String url = request.getUrl();
        int timeout = Math.max(connectTimeout, soTimeout);
        HttpTransportSE transport = new HttpTransportSE(url, timeout);
//		transport.debug = true;
        try {
            transport.call(request.getActionName(), envelope, headerProperties);
            return new WebServiceResponse(envelope);
        } catch (Exception e) {
            String msg = e.getMessage();
            return new ErrorResponse(msg);
        }
    }

    protected SoapSerializationEnvelope createEnvelope(SoapObject request) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request); // 等价于envelope.bodyOut = rpc;
        envelope.dotNet = false; // 不是dotNet开发的WebService
        return envelope;
    }


}
