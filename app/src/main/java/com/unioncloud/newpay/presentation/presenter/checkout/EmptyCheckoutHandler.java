package com.unioncloud.newpay.presentation.presenter.checkout;

import com.unioncloud.newpay.domain.interactor.cart.EmptyCartInteractor;
import com.unioncloud.newpay.presentation.presenter.BooleanUpdateHandler;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/10.
 */
public class EmptyCheckoutHandler extends BooleanUpdateHandler implements Runnable {

    public EmptyCheckoutHandler(boolean isUpdating) {
        super(isUpdating);
    }

    @Override
    public void run() {
        EmptyCartInteractor interactor = new EmptyCartInteractor(
                PresenterUtils.getExecutorProvider(),
                PresenterUtils.getCartDataRepository());
        interactor.execute(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                data = aBoolean;
                CheckoutDataManager.getInstance().clear();
                onUpdateCompleted();
            }
        });
    }

    @Override
    public boolean isSuccess() {
        return super.isSuccess() && getData();
    }
}
