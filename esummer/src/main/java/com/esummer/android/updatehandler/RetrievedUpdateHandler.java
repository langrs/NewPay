package com.esummer.android.updatehandler;

/**
 * Created by cwj on 16/8/8.
 */
public class RetrievedUpdateHandler<DataType> extends UpdateHandler<DataType, RetrievedUpdateHandler<DataType>> {

    private boolean hasBeenRetrieved = false;

    public RetrievedUpdateHandler(DataType data, boolean isUpdating) {
        super(data, isUpdating);
    }

    @Override
    public boolean callOrSubscribe(UpdateCompleteCallback<RetrievedUpdateHandler<DataType>> listener) {
        synchronized (getStatusLock()) {
            if (hasBeenRetrieved) {
                listener.onCompleted(this, isSuccess());
                return true;
            } else {
                addCompletionListener(listener);
                return false;
            }
        }
    }

    @Override
    public boolean callOrSubscribe(UpdateSuccessFailedCallback<RetrievedUpdateHandler<DataType>> listener) {
        synchronized (getStatusLock()) {
            if (hasBeenRetrieved) {
                if (isSuccess()) {
                    listener.onSuccess(this);
                } else {
                    listener.onFailed(this);
                }
                return true;
            } else {
                addSuccessFailureListener(listener);
                return false;
            }
        }
    }

    public boolean hasBeenRetrieved() {
        return hasBeenRetrieved;
    }

    @Override
    protected void onUpdateCompleted() {
        synchronized (getStatusLock()) {
            hasBeenRetrieved = true;
            super.onUpdateCompleted();
        }
    }
}


