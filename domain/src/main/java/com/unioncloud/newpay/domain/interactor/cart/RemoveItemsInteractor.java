package com.unioncloud.newpay.domain.interactor.cart;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.repository.CartRepository;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 16/8/9.
 */
public class RemoveItemsInteractor extends BaseInteractor<Boolean> {

    private List<CartItem> itemList;
    private CartRepository repository;

    public RemoveItemsInteractor(ExecutorProvider provider,
                                 CartItem cartItem,
                                 CartRepository repository) {
        super(provider);
        this.itemList = new ArrayList<>(1);
        itemList.add(cartItem);
        this.repository = repository;
    }

    public RemoveItemsInteractor(ExecutorProvider provider,
                                 List<CartItem> cartItems,
                                 CartRepository repository) {
        super(provider);
        this.itemList = new ArrayList<>(itemList);
        this.repository = repository;
    }

    @Override
    protected Observable<Boolean> bindObservable() {
        return repository.removeItems(itemList);
    }
}
