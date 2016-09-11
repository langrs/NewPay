package com.unioncloud.newpay.presentation.presenter.cart;

import com.esummer.android.updatehandler.RetrievedUpdateHandler;
import com.unioncloud.newpay.domain.interactor.cart.UpdateCartInteractor;
import com.unioncloud.newpay.domain.model.cart.CartData;
import com.unioncloud.newpay.presentation.model.cart.CartDataManager;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/10.
 */
public class UpdateCartHandler extends RetrievedUpdateHandler<CartDataManager>
        implements Runnable {

    public UpdateCartHandler(CartDataManager data, boolean isUpdating) {
        super(data, isUpdating);
    }

    @Override
    public void run() {
        UpdateCartInteractor interactor = new UpdateCartInteractor(
                PresenterUtils.getExecutorProvider(),
                PresenterUtils.getCartDataRepository());
        interactor.execute(new Subscriber<CartData>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(CartData cartData) {
                // 没有根据返回的CartData更新本地购物车
                data = CartDataManager.getInstance();
                onUpdateCompleted();
            }
        });
    }
}
