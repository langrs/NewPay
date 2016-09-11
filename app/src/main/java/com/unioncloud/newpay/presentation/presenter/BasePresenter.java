package com.unioncloud.newpay.presentation.presenter;


import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.executor.DefaultExecutorProvider;

/**
 * Created by cwj on 16/8/9.
 */
public abstract class BasePresenter implements Presenter {

    protected ExecutorProvider getExecutorProvider() {
        return DefaultExecutorProvider.getInstance();
    }

}
