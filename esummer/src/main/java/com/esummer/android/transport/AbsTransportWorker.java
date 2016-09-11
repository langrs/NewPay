package com.esummer.android.transport;

import com.raizlabs.coreutils.collections.MappableSet;
import com.raizlabs.coreutils.functions.Delegate;
import com.raizlabs.coreutils.listeners.ProgressListener;

import java.util.HashSet;

/**
 * Created by cwj on 16/7/15.
 */
public abstract class AbsTransportWorker<ResultType> implements TransportWorker<ResultType> {

    private TransportRequest.RequestBuilder requestBuilder;

    private WorkerListener<ResultType> resultListener = new WorkerListener<>();

    private HashSet<ProgressListener> progressListeners = new HashSet<>();
    private boolean b = false;
    private boolean isTransportFinish = false;

//    com.raizlabs.net.b.k<ResultType>
    private class WorkerListener<ResultType> extends MappableSet<Callback<ResultType>> {
        public void mapToRequest(final TransportWorker<ResultType> worker) {
            map(new Delegate<Callback<ResultType>>() {
                @Override
                public void execute(Callback<ResultType> callback) {
                    callback.onWorkerFinish(worker);
                }
            });
        }
    }

    public void notifiProgress(int currentProgress, int maxProgress) {
        synchronized (progressListeners) {
            for (ProgressListener listener : progressListeners) {
                listener.onProgressUpdate(currentProgress, maxProgress);
            }
        }
    }

    public void addProgressListener(ProgressListener listener) {
        synchronized (progressListeners) {
            progressListeners.add(listener);
        }
    }

    public void removeProgressListener(ProgressListener listener) {
        synchronized (progressListeners) {
            progressListeners.remove(listener);
        }
    }

    @Override
    public void addWorkerListener(Callback<ResultType> callback) {
        synchronized (getStatusLock()) {
            resultListener.add(callback);
            if (isTransportFinish) {
                callback.onWorkerFinish(this);
            }
        }
    }

    @Override
    public boolean removeWorkerListener(Callback<ResultType> callback) {
        synchronized (getStatusLock()) {
            return resultListener.remove(callback);
        }
    }

    @Override
    public Object getStatusLock() {
        return this;
    }


    protected abstract TransportRequest.RequestBuilder createRequestBuilder();

    @Override
    public void cancel() {

    }

    public void onFinish() {
       synchronized (getStatusLock()) {
           this.isTransportFinish = true;
           resultListener.mapToRequest(this);
       }
    }

    @Override
    public boolean isTransportFinish() {
        synchronized (getStatusLock()) {
            boolean isFinish = this.isTransportFinish;
            return isFinish;
        }
    }

    @Override
    public TransportRequest getRequest() {
        return createRequestBuilder().builder();
    }
}
