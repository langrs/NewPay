package com.unioncloud.newpay.presentation.presenter.testp;

import android.os.Looper;
import android.util.Log;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.data.repository.testp.TestpDataRepository;
import com.unioncloud.newpay.domain.interactor.testp.QueryTestpInteractor;
import com.unioncloud.newpay.domain.model.testp.Testp;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

public class QueryTestpHandler extends UpdateHandler<Testp,QueryTestpHandler>
implements Runnable{
    Testp testp;
    public QueryTestpHandler(Testp _testp, boolean isUpdating) {
        super(null, isUpdating);
        testp = _testp;
    }

    @Override
    public void run() {
        try{
            Log.i("-----QueryTestp----","start....");
            Thread.sleep(5000);
            data = testp;
//            onUpdateCompleted();
            onUpdateFailed();
        }catch (Exception e){

        }
//        QueryTestpInteractor queryTestpInteractor =
//                new QueryTestpInteractor(PresenterUtils.getExecutorProvider(),testp,new TestpDataRepository());
//        queryTestpInteractor.execute(new Subscriber<Testp>(){
//            @Override
//            public void onCompleted() {
//                unsubscribe();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                onUpdateFailed();
//            }
//
//            @Override
//            public void onNext(Testp testp) {
//                data = testp;
//                onUpdateCompleted();
//            }
//        });
//
    }
}