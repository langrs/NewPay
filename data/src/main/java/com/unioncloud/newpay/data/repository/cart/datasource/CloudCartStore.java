package com.unioncloud.newpay.data.repository.cart.datasource;

import com.unioncloud.newpay.domain.model.cart.CartData;
import com.unioncloud.newpay.domain.model.cart.CartItem;

import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 16/8/13.
 */
public class CloudCartStore implements CartStore {

    @Override
    public Observable<Boolean> removeItems(List<CartItem> list) {
        return null;
    }

    @Override
    public Observable<Boolean> emptyCart() {
        return null;
    }

    @Override
    public Observable<CartData> updateCartData() {
        return null;
    }
}
