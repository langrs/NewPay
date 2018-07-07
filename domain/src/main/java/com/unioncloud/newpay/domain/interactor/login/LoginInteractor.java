package com.unioncloud.newpay.domain.interactor.login;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.login.LoginResult;
import com.unioncloud.newpay.domain.model.login.User;
import com.unioncloud.newpay.domain.model.login.UserLogin;
import com.unioncloud.newpay.domain.repository.LoginRepository;

import rx.Observable;

/**
 * Created by cwj on 16/6/29.
 */
public class LoginInteractor extends BaseInteractor<LoginResult> {

    private LoginRepository repository;
    private UserLogin userLogin;

    public LoginInteractor(ExecutorProvider provider,
                           UserLogin login,
                           LoginRepository repository) {
//        调用父类的构造函数BaseInteractor
        super(provider);
        this.userLogin = login;
        this.repository = repository;
    }
//这个主要是指定了被观察者,耗时操作,主要的在repository中
    @Override
    protected Observable<LoginResult> bindObservable() {
        return repository.login(userLogin);
    }
//父类的execute方法中,参数传入观察者,该方法绑定了被观察者和观察者,同时也指定了不同的线程
}
