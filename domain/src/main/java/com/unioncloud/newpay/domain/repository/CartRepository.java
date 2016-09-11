package com.unioncloud.newpay.domain.repository;

import com.unioncloud.newpay.domain.model.cart.AddProductEntry;
import com.unioncloud.newpay.domain.model.cart.CartData;
import com.unioncloud.newpay.domain.model.cart.CartItem;

import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 16/8/9.
 */
public interface CartRepository {

    Observable<Boolean> addItem(AddProductEntry entry);

    Observable<Boolean> removeItems(List<CartItem> list);

    Observable<Boolean> emptyCart();

    Observable<CartData> updateCartData();

    Observable<Boolean> updateItemQuantity(CartItem cartItem, int newQuantity);

    Observable<Boolean> updateItemSellUnitPrice(CartItem cartItem, int newPrice);
}
