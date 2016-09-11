package com.unioncloud.newpay.domain.interactor.login;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.login.ChangePassword;
import com.unioncloud.newpay.domain.repository.LoginRepository;

import rx.Observable;

/**
 * Created by cwj on 16/9/5.
 */
public class ChangePasswordInteractor extends BaseInteractor<Boolean> {

    private ChangePassword changePassword;
    private LoginRepository repository;

    public ChangePasswordInteractor(ExecutorProvider provider,
                                    ChangePassword changePassword,
                                    LoginRepository repository) {
        super(provider);
        this.changePassword = changePassword;
        this.repository = repository;
    }

    @Override
    protected Observable<Boolean> bindObservable() {
        return repository.changePassword(
                changePassword.getShopId(),
                changePassword.getUserId(),
                changePassword.getNewPassword(),
                changePassword.getOldPassword());
    }
}
