package com.unioncloud.newpay.presentation.presenter.login;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.login.LoginInteractor;
import com.unioncloud.newpay.domain.model.login.LoginResult;
import com.unioncloud.newpay.domain.model.login.UserLogin;
import com.unioncloud.newpay.presentation.model.ResultData;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/13.
 */
public class LoginHandler extends UpdateHandler<ResultData<Void>, LoginHandler>
        implements Runnable {
//1.定义需要处理的VO对象,该对象通过构造函数传入
    private UserLogin userLogin;

    public LoginHandler(UserLogin userLogin, boolean isUpdating) {
        super(new ResultData<Void>(false, null, ""), isUpdating);
        this.userLogin = userLogin;
    }
//2.主要需要实现run的线程耗时操作
    @Override
    public void run() {
//3.
        LoginInteractor interactor = new LoginInteractor(
                PresenterUtils.getExecutorProvider(),
                userLogin,
                PresenterUtils.getLoginRepository());
        interactor.execute(new Subscriber<LoginResult>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                data.onFailed(e.getMessage());
                onUpdateFailed();
            }

            @Override
            public void onNext(LoginResult loginResult) {
                PosDataManager.getInstance().onLogin(loginResult);
                data.onSuccess(null);
                onUpdateCompleted();
            }
        });
    }
}
