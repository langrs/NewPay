package com.unioncloud.newpay.presentation.presenter.register;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.pos.RegisterPosInteractor;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.pos.PosRegister;
import com.unioncloud.newpay.presentation.model.ResultData;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/12.
 */
public class RegisterPosHandler extends UpdateHandler<ResultData<Void>, RegisterPosHandler>
        implements Runnable {

    private PosRegister registerInfo;

    public RegisterPosHandler(boolean isUpdating, PosRegister registerInfo) {
        super(new ResultData<Void>(), isUpdating);
        this.registerInfo = registerInfo;
    }

    @Override
    public void run() {
        RegisterPosInteractor interactor = new RegisterPosInteractor(
                PresenterUtils.getExecutorProvider(),
                registerInfo,
                PresenterUtils.getPosRepository());
        interactor.execute(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                data = new ResultData<Void>(false, null, e.getMessage());
                onUpdateFailed();
            }

            @Override
            public void onNext(Boolean isSuccess) {
                data = new ResultData<Void>(isSuccess, null, "");
                onUpdateCompleted();
            }
        });
    }
}
