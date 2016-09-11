package com.unioncloud.newpay.presentation.presenter.cart;

import com.unioncloud.newpay.domain.interactor.cart.UpdateItemQuantityInteractor;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.presentation.presenter.BooleanUpdateHandler;
import com.unioncloud.newpay.presentation.model.cart.CartDataManager;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/14.
 */
public class UpdateItemQuantityHandler extends BooleanUpdateHandler implements Runnable {

    private CartItem cartItem;
    private int newQuantity;

    public UpdateItemQuantityHandler(CartItem cartItem, int newQuantity) {
        super(true);
        this.cartItem = cartItem;
        this.newQuantity = newQuantity;
    }

    @Override
    public void run() {
        UpdateItemQuantityInteractor interactor = new UpdateItemQuantityInteractor(
                PresenterUtils.getExecutorProvider(),
                cartItem, newQuantity,
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
                CartDataManager.getInstance().updateLocalItemQuantity(cartItem, newQuantity);
                onUpdateCompleted();
            }
        });
    }
}
