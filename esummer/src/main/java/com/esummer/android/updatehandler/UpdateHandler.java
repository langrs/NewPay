package com.esummer.android.updatehandler;

import android.os.Looper;
import android.util.Log;

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
//            针对这个回调函数基本上是不做处理的
            this.successFailureListeners.map(this.updateCompleteCallback);
//            针对这个回调函数,都是进行调用,而忽略了第二个参数,第二个参数是表明了成功还是失败
//            由于是和下面的onUpdateFailed的实现类不一样,所以本方法处理的是针对所有
// 通过addCompletionListener注册的UpdateCompleteCallBack进行循环调用其onComplete方法,并且第二个
//            入参固定传入为true,而onUpdateFailed的入参固定传入为false
//            这里还有一点重点需要理解的是this.completionSuccessCallback这个入参其实是实现类来的
//            而且他是Delege的实现类,通过不同的实现类来改变不同的用法
//            但是在这里的处理中,其实这个不重要了,因为都没有通过他来处理,所以总结出来
//            onUpdateComplete和onUpdateFailed的最大的不同是把isSuccess设置为不同的值,然后交由
//            回调函数去判断这个response的isSuccess标志来得到成功还是失败,而跟返回的第二个参数无关
//            所以在程序中一致通过判断isSuccess就能知道返回的结果是否成功了
//            也就是入参这2个方法都可以设置为this.completionSuccessCallback或都设置
//              为this.completionFailedCallback,没有区别,就是为了调用回调函数而已,这2个实现类其实都
//            可以调用这个回调函数的
            this.completionListeners.map(this.completionSuccessCallback);
        }
    }

    protected void onUpdateFailed() {
        synchronized (getStatusLock()) {
            this.isUpdating = false;
            this.isSuccess = false;
            //            针对这个回调函数基本上是不做处理的
            this.successFailureListeners.map(this.updateFailedCallback);
            //            针对这个回调函数,都是进行调用,而忽略了第二个参数,第二个参数是表明了成功还是失败
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
