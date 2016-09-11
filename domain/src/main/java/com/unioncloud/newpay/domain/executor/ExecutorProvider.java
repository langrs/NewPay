package com.unioncloud.newpay.domain.executor;

import java.util.concurrent.Executor;

import rx.Scheduler;

/**
 * Created by cwj on 16/6/27.
 */
public interface ExecutorProvider {

    Executor providerThread();

    Scheduler providerMainScheduler();

}
