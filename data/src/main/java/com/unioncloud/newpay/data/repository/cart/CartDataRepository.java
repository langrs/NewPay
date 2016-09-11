package com.unioncloud.newpay.data.repository.cart;

import com.unioncloud.newpay.domain.model.cart.AddProductEntry;
import com.unioncloud.newpay.domain.model.cart.CartData;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.repository.CartRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 16/8/13.
 */
public class CartDataRepository implements CartRepository {

    @Override
    public Observable<Boolean> addItem(AddProductEntry entry) {
        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> removeItems(List<CartItem> list) {
        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> emptyCart() {
        return Observable.just(true);
    }

    @Override
    public Observable<CartData> updateCartData() {
        return Observable.just(null);
    }

    @Override
    public Observable<Boolean> updateItemQuantity(CartItem cartItem, int newQuantity) {
        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> updateItemSellUnitPrice(CartItem cartItem, int newPrice) {
        return Observable.just(true);
    }
}
