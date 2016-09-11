package com.unioncloud.newpay.data.repository.cart.datasource;

import com.unioncloud.newpay.domain.model.cart.CartData;
import com.unioncloud.newpay.domain.model.cart.CartItem;

import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 16/8/13.
 */
public interface CartStore {

    Observable<Boolean> removeItems(List<CartItem> list);

    Observable<Boolean> emptyCart();

    Observable<CartData> updateCartData();
}
