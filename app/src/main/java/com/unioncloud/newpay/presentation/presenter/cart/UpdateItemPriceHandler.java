package com.unioncloud.newpay.presentation.presenter.cart;

import com.unioncloud.newpay.domain.interactor.cart.UpdateItemPriceInteractor;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.presentation.presenter.BooleanUpdateHandler;
import com.unioncloud.newpay.presentation.model.cart.CartDataManager;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/14.
 */
public class UpdateItemPriceHandler extends BooleanUpdateHandler
        implements Runnable {

    private CartItem cartItem;
    private int newPrice;

    public UpdateItemPriceHandler(CartItem cartItem, int newPrice) {
        super(true);
        this.cartItem = cartItem;
        this.newPrice = newPrice;
    }

    @Override
    public void run() {
        UpdateItemPriceInteractor interactor = new UpdateItemPriceInteractor(
                PresenterUtils.getExecutorProvider(),
                cartItem, newPrice,
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
                CartDataManager.getInstance().updateLocalItemUnitPrice(cartItem, newPrice);
                onUpdateCompleted();
            }
        });
    }
}
