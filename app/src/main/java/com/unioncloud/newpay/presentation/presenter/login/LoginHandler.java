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
//需要注意的是这个线程是运行在子线程,因为是在fragment中run的该handle的线程方法,而针对
//    iteractor的execute,因为传入进去了一个线程对象PresenterUtils.getExecutorProvider,
//    那么观察者是运行在了主线程上的
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
//响应耗时的观察者方法,被执行在主线程
            @Override
            public void onNext(LoginResult loginResult) {
//                通过单例对象保存全局变量
                PosDataManager.getInstance().onLogin(loginResult);
//                由于这个是继承于UpdateHandler的接口的,而最终
                data.onSuccess(null);
                onUpdateCompleted();
            }
        });
    }
}
