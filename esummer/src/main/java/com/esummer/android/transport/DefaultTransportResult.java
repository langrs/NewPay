package com.esummer.android.transport;

import java.io.IOException;
import java.util.Date;

/**
 * Created by cwj on 16/7/18.
 */
public class DefaultTransportResult<ResultType> implements TransportResult<ResultType> {

    Date time;
    ResultType data;
    int responseCode = -1;
    boolean isComplete = false;
    String responseMessage;

    public DefaultTransportResult(ResultType data, Date time) {
        this.data = data;
        this.time = time;
    }

    public DefaultTransportResult(ResultType data, Date responseDate, TransportResponse response) {
        this(data, responseDate);
        init(response);
    }

    private void init(TransportResponse response)  {
        if (response != null) {
            try {
                this.responseCode = response.getResponseCode();
                this.responseMessage = response.getResponseMessage();
            } catch (IOException e) {
                System.out.println("http response read fail because IOException");;
            }
        }
    }

    @Override
    public ResultType getResultData() {
        return data;
    }

    @Override
    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    @Override
    public boolean isSuccess() {
        return ResponseStateUtils.isSuccess(responseCode);
    }
}
