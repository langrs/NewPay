package com.unioncloud.newpay.domain.interactor;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by cwj on 16/6/27.
 */
public abstract class BaseInteractor<T> {

    private ExecutorProvider provider;

    private Subscription subscription = Subscriptions.empty();

    public BaseInteractor(ExecutorProvider provider) {
        this.provider = provider;
    }

    protected abstract Observable<T> bindObservable();

    public void execute(Subscriber<T> domainSubscriber) {
        subscription = bindObservable().
                subscribeOn(Schedulers.from(provider.providerThread())).
                observeOn(provider.providerMainScheduler()).
                subscribe(domainSubscriber);
    }

    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
