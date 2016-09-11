package com.unioncloud.newpay.presentation.presenter.checkout;

import android.text.TextUtils;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.NewPayApplication;
import com.unioncloud.newpay.domain.interactor.checkout.GetOrderSerialNumberInteractor;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.presentation.model.checkout.UsedOrderSerialNumber;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;
import com.unioncloud.newpay.presentation.presenter.sharedpreferences.PreferencesUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/11.
 */
public class GetOrderSerialNumberHandler
        extends UpdateHandler<UsedOrderSerialNumber, GetOrderSerialNumberHandler>
        implements Runnable {

    private PosInfo posInfo;

    public GetOrderSerialNumberHandler(UsedOrderSerialNumber lastOrderSerialNumber, PosInfo info, boolean isUpdating) {
        super(lastOrderSerialNumber, isUpdating);
        this.posInfo = info;
    }

    @Override
    public void run() {
        if (!getLocalSerialNumber()) {
            getRemoteSerialNumber();
        }
    }

    private void getRemoteSerialNumber() {
        GetOrderSerialNumberInteractor interactor = new GetOrderSerialNumberInteractor(
                PresenterUtils.getExecutorProvider(),
                posInfo,
                PresenterUtils.getSaleOrderRepository());
        interactor.execute(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(String s) {
                getData().setSerialNumber(s);
                onUpdateCompleted();
            }
        });
    }

    private boolean getLocalSerialNumber() {
        String orderSerialNumber = PreferencesUtils.getOrderSerialNumber(
                NewPayApplication.getInstance());
        if (!TextUtils.isEmpty(orderSerialNumber)) {
            getData().setSerialNumber(orderSerialNumber);
            onUpdateCompleted();
            return true;
        }
        return false;
    }
}
