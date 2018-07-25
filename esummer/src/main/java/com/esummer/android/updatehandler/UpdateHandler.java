package com.esummer.android.updatehandler;

import com.raizlabs.coreutils.collections.MappableSet;
import com.raizlabs.coreutils.functions.Delegate;

/**
 * Created by cwj on 16/8/7.
 */
public class UpdateHandler<DataType, ResponseType extends UpdateHandler<DataType, ?>>
    implements IUpdateHandler<DataType, ResponseType> {

    private MappableSet<UpdateCompleteCallback<ResponseType>> completionListeners;
    private Delegate<UpdateCompleteCallback<ResponseType>> completionFailedCallback
            = new Delegate<UpdateCompleteCallback<ResponseType>>() {
        @Override
        public void execute(UpdateCompleteCallback<ResponseType> callback) {
            callback.onCompleted((ResponseType)UpdateHandler.this, false);
        }
    };
    private Delegate<UpdateCompleteCallback<ResponseType>> completionSuccessCallback
            = new Delegate<UpdateCompleteCallback<ResponseType>>() {
        @Override
        public void execute(UpdateCompleteCallback<ResponseType> callback) {
            callback.onCompleted((ResponseType)UpdateHandler.this, true);
        }
    };
//可以通过构造函数指定货后期响应后赋值的返回类型
    protected DataType data;
    private boolean isSuccess;
    private volatile boolean isUpdating;

    private MappableSet<UpdateSuccessFailedCallback<ResponseType>> successFailureListeners;
    private Delegate<UpdateSuccessFailedCallback<ResponseType>> updateCompleteCallback
            = new Delegate<UpdateSuccessFailedCallback<ResponseType>>() {
        @Override
        public void execute(UpdateSuccessFailedCallback<ResponseType> callback) {
            callback.onSuccess((ResponseType)UpdateHandler.this);
        }
    };
    private Delegate<UpdateSuccessFailedCallback<ResponseType>> updateFailedCallback
            = new Delegate<UpdateSuccessFailedCallback<ResponseType>>() {
        @Override
        public void execute(UpdateSuccessFailedCallback<ResponseType> callback) {
            callback.onFailed((ResponseType)UpdateHandler.this);
        }
    };

    public UpdateHandler(DataType data, boolean isUpdating) {
        this.data = data;
        this.isUpdating = isUpdating;
        completionListeners = new MappableSet<>();
        successFailureListeners = new MappableSet<>();
        this.isSuccess = false;
    }

    public void addCompletionListener(UpdateCompleteCallback<ResponseType> listener) {
        completionListeners.add(listener);
    }

    public void addSuccessFailureListener(UpdateSuccessFailedCallback<ResponseType> listener) {
        successFailureListeners.add(listener);
    }

    public boolean callOrSubscribe(UpdateCompleteCallback<ResponseType> listener) {
        synchronized (getStatusLock()) {
            if (isUpdating()) {
                addCompletionListener(listener);
                return false;
            } else {
                listener.onCompleted((ResponseType)this, isSuccess());
                return true;
            }
        }
    }

    public boolean callOrSubscribe(UpdateSuccessFailedCallback<ResponseType> listener) {
        synchronized (getStatusLock()) {
            if (isUpdating()) {
                addSuccessFailureListener(listener);
                return false;
            }
            if (isSuccess()) {
                listener.onSuccess((ResponseType)this);
            } else {
                listener.onFailed((ResponseType)this);
            }
            return true;
        }
    }

    public Object getStatusLock() {
        return this;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public boolean isUpdating() {
        synchronized (getStatusLock()) {
            return this.isUpdating;
        }
    }

    public DataType getData() {
        return this.data;
    }

    protected void onUpdateCompleted() {
        synchronized (getStatusLock()) {
            this.isUpdating = false;
            this.isSuccess = true;
            this.successFailureListeners.map(this.updateCompleteCallback);
            this.completionListeners.map(this.completionSuccessCallback);
        }
    }

    protected void onUpdateFailed() {
        synchronized (getStatusLock()) {
            this.isUpdating = false;
            this.isSuccess = false;
            this.successFailureListeners.map(this.updateFailedCallback);
            this.completionListeners.map(this.completionFailedCallback);
        }
    }

    public void removeCompletionListener(UpdateCompleteCallback<ResponseType> listener) {
        this.completionListeners.remove(listener);
    }

    public void removeSuccessFailureListener(UpdateSuccessFailedCallback<ResponseType> listener) {
        this.successFailureListeners.remove(listener);
    }

    protected void setIsUpdating(boolean isUpdating) {
        synchronized (getStatusLock()) {
            this.isUpdating = isUpdating;
        }
    }

    protected boolean startUpdate() {
        synchronized (getStatusLock()) {
            if (isUpdating()) {
                return false;
            } else {
                setIsUpdating(true);
                return true;
            }
        }
    }
}
