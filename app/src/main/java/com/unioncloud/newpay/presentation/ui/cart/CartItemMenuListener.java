package com.unioncloud.newpay.presentation.ui.cart;

import android.view.View;

import com.unioncloud.newpay.domain.model.cart.CartItem;

/**
 * Created by cwj on 16/8/13.
 */
public interface CartItemMenuListener {

    void onItemMenu(View view);

    void onItemQuantityChanged(CartItem cartItem, int newQuantity);

}
