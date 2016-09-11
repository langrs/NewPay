package com.unioncloud.newpay.data.net.webservice;

import com.raizlabs.coreutils.listeners.ProgressListener;
import com.unioncloud.newpay.data.net.NetResponse;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by cwj on 16/8/8.
 */
public class WebServiceResponse implements NetResponse {

    private String errorMessage;
    private String singleResult;
//    private List<String> multiResult;
    private int responseCode;

    public WebServiceResponse(SoapSerializationEnvelope envelope) {
        if (envelope.bodyIn instanceof SoapFault) {
            this.errorMessage = ((SoapFault) envelope.bodyIn).getMessage();
            responseCode = 0;
        } else {
            SoapObject result = (SoapObject) envelope.bodyIn;
            singleResult = String.valueOf(result.getProperty(0));
            responseCode = 200;
        }
    }

    @Override
    public String getStringData() {
        return singleResult;
    }

    @Override
    public boolean getFileData(File target, ProgressListener listener) {
        return false;
    }

    @Override
    public int getResponseCode() throws IOException {
        return responseCode;
    }

    @Override
    public String getResponseMessage() throws IOException {
        return errorMessage;
    }

    @Override
    public long getContentLength() {
        return singleResult.length();
    }

    @Override
    public InputStream getInputStreamData() throws IOException {
        if (singleResult != null) {
            return new ByteArrayInputStream(singleResult.getBytes());
        }
        return null;
    }

    @Override
    public void disconnect() {

    }
}
