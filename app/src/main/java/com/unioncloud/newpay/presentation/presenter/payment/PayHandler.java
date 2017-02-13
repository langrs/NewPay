package com.unioncloud.newpay.presentation.presenter.payment;

import com.esummer.android.updatehandler.UpdateHandler;

public abstract class PayHandler<D, ResponseType extends PayHandler<D, ?>> extends UpdateHandler<D, ResponseType> {
    public PayHandler(D data, boolean isUpdating) {
        super(data, isUpdating);
    }

    public abstract void startPay();

}
