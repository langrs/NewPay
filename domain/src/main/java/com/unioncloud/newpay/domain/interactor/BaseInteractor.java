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
//Observable调用subscribe( )方法返回的对象，有unsubscribe( )方法，可以用来取消订阅事件
    private Subscription subscription = Subscriptions.empty();

    public BaseInteractor(ExecutorProvider provider) {
        this.provider = provider;
    }
//通过子类来获取被观察者
    protected abstract Observable<T> bindObservable();
//接收观察者参数,在子类的bindObservable中设置好被观察者,在该方法中传入观察者,通过该方法来关联
//观察者和被观察者,并且两者开启不同的线程来运行,
    public void execute(Subscriber<T> domainSubscriber) {
//        subscribeOn指定被观察者Observable在哪个线程上
//        observeOn指定观察者Subscriber or Observe 在哪个线程上
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
