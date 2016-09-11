package com.unioncloud.newpay.domain.interactor.cart;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.cart.CartData;
import com.unioncloud.newpay.domain.repository.CartRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/10.
 */
public class UpdateCartInteractor extends BaseInteractor<CartData> {

    private CartRepository repository;

    public UpdateCartInteractor(ExecutorProvider provider,
                                CartRepository repository) {
        super(provider);
        this.repository = repository;
    }

    @Override
    protected Observable<CartData> bindObservable() {
        return repository.updateCartData();
    }
}
