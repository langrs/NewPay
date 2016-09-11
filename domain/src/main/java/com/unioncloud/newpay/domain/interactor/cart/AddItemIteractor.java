package com.unioncloud.newpay.domain.interactor.cart;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.cart.AddProductEntry;
import com.unioncloud.newpay.domain.repository.CartRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/14.
 */
public class AddItemIteractor extends BaseInteractor<Boolean> {

    private CartRepository repository;
    private AddProductEntry entry;

    public AddItemIteractor(ExecutorProvider provider,
                            AddProductEntry entry,
                            CartRepository repository) {
        super(provider);
        this.entry = entry;
        this.repository = repository;
    }

    @Override
    protected Observable<Boolean> bindObservable() {
        return repository.addItem(entry);
    }
}
