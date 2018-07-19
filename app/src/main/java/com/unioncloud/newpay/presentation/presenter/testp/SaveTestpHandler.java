package com.unioncloud.newpay.presentation.presenter.testp;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.data.repository.testp.TestpDataRepository;
import com.unioncloud.newpay.domain.interactor.testp.SaveTestpInteractor;
import com.unioncloud.newpay.domain.model.testp.Testp;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

public class SaveTestpHandler extends UpdateHandler<Boolean,SaveTestpHandler>
implements Runnable{
    private Testp testp;
    public SaveTestpHandler(Boolean data, Testp _testp, boolean isUpdating) {
        super(data, isUpdating);
        this.testp = _testp;
    }

    @Override
    public void run() {
        SaveTestpInteractor saveTestpInteractor =
                new SaveTestpInteractor(PresenterUtils.getExecutorProvider(),testp,new TestpDataRepository());
        saveTestpInteractor.execute(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(Boolean o) {
                data = true;
                onUpdateCompleted();
            }
        });
    }
}