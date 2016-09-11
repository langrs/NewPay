package com.esummer.android.transport;

/**
 * Created by cwj on 16/7/15.
 */
public interface TransportWorker<ResultType> extends RequestProvider{

    interface Callback<ResultType> {

        void onWorkerFinish(TransportWorker<ResultType> worker);

    }

    ResultType parseResultData(TransportResponse response);

//    ResultType parseResultData(HttpResponse response, TransportMethod method);

    void addWorkerListener(Callback<ResultType> callback);

    boolean removeWorkerListener(Callback<ResultType> callback);

    Object getStatusLock();

    void executeTransport(Transport transport);

    void cancel();

    boolean isTransportFinish();
}
