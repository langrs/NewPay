package com.unioncloud.newpay.domain.interactor.cart;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.repository.CartRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/14.
 */
public class UpdateItemPriceInteractor extends BaseInteractor<Boolean> {

    private CartRepository repository;
    private CartItem cartItem;
    private int newPrice;

    public UpdateItemPriceInteractor(ExecutorProvider provider,
                                     CartItem cartItem,
                                     int newPrice,
                                     CartRepository repository) {
        super(provider);
        this.cartItem = cartItem;
        this.newPrice = newPrice;
    }

    @Override
    protected Observable<Boolean> bindObservable() {
        return repository.updateItemSellUnitPrice(cartItem, newPrice);
    }
}
