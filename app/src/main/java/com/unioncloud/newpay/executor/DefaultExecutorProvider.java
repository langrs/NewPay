package com.unioncloud.newpay.executor;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;

import java.util.concurrent.Executor;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by cwj on 16/8/8.
 */
public class DefaultExecutorProvider implements ExecutorProvider {

    private static DefaultExecutorProvider instance;

    private ThreadExecutor threadExecutor;

    public DefaultExecutorProvider() {
        this.threadExecutor = new ThreadExecutor();
    }

    public static DefaultExecutorProvider getInstance() {
        if (instance == null) {
            instance = new DefaultExecutorProvider();
        }
        return instance;
    }

    @Override
    public Executor providerThread() {
        return threadExecutor;
    }

    @Override
    public Scheduler providerMainScheduler() {
        return AndroidSchedulers.mainThread();
    }

}
