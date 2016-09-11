package com.unioncloud.newpay.domain.interactor.cart;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.repository.CartRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/14.
 */
public class UpdateItemQuantityInteractor extends BaseInteractor<Boolean> {
    private CartRepository repository;
    private CartItem cartItem;
    private int newQuantity;

    public UpdateItemQuantityInteractor(ExecutorProvider provider,
                                        CartItem cartItem,
                                        int newQuantity,
                                        CartRepository repository) {
        super(provider);
        this.cartItem = cartItem;
        this.newQuantity = newQuantity;
        this.repository = repository;
    }

    @Override
    protected Observable<Boolean> bindObservable() {
        return repository.updateItemQuantity(cartItem, newQuantity);
    }
}
