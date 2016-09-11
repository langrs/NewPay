package com.esummer.android.transport;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;

/**
 * Created by cwj on 16/7/15.
 */
public interface TransportRequest {

     interface RequestBuilder {

        RequestBuilder setUri(URI uri);

        RequestBuilder setUri(String uri);

        RequestBuilder setHttpMethod(TransportMethod method);

        RequestBuilder setPostParam(Map<String, String> postParam);

        RequestBuilder setGetParam(Map<String, String> postParam);

        RequestBuilder setInputStreamParam(InputStream inputStream);

        RequestBuilder setPriority(int priority);

        TransportRequest builder();

    }

    URI getUri();

    String getUriAsString();

    TransportMethod getHttpMethod();

    Map<String, String> getRequestParam();

    InputStream getInputStreamParam();

    int getPriority();

    void abort();
}
