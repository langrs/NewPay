package com.unioncloud.newpay.data.net;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by cwj on 16/8/8.
 */
public class NetRequest {

    private LinkedHashMap<String, String> paramList;

    private String url;
    private String namespace;
    private String actionName;
    private NetMethod method;
    private int priority = 10;

    private HttpTransportSE transportSE;

    public NetRequest(String url, String namespace, String actionName) {
        this.url = url;
        this.namespace = namespace;
        this.actionName = actionName;
        paramList = new LinkedHashMap<>();
    }

    public NetRequest(String url, String namespace, String actionName, Map<String, String> paramMap) {
        this.url = url;
        this.namespace = namespace;
        this.actionName = actionName;
        paramList = new LinkedHashMap<>(paramMap);
    }


    public String getUrl() {
        return this.url;
    }

    public String getActionName() {
        return this.actionName;
    }

    public NetMethod getHttpMethod() {
        return method;
    }

    public void addParam(String key, String value) {
        paramList.put(key, value);
    }

    public Map<String, String> getRequestParam() {
        return paramList;
    }

    public InputStream getInputStreamParam() {
        return null;
    }

    public int getPriority() {
        return priority;
    }

    public void abort() {
//        try {
//            getTransport().getConnection().disconnect();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

//    protected HttpTransportSE getTransport() {
//        synchronized (this) {
//            if (transportSE == null) {
//                transportSE = new HttpTransportSE(url);
//            }
//            return transportSE;
//        }
//    }

    public SoapObject getSoapRequest() {
        SoapObject soapObject = new SoapObject(namespace, actionName);

        Map<String, String> params = getRequestParam();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                soapObject.addProperty(entry.getKey(), entry.getValue());
            }
        }
        return soapObject;
    }
}
