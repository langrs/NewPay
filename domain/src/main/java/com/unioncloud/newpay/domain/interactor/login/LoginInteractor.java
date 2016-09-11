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
        super(provider);
        this.userLogin = login;
        this.repository = repository;
    }

    @Override
    protected Observable<LoginResult> bindObservable() {
        return repository.login(userLogin);
    }
}
