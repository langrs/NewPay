package com.unioncloud.newpay.presentation.ui.cart.views;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.raizlabs.universaladapter.ListBasedAdapter;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.presentation.ui.cart.CartItemMenuListener;

import java.util.Collection;

/**
 * Created by cwj on 16/8/14.
 */
public class CartItem2Adapter extends ListBasedAdapter<CartItem, CartItemViewHolder> {
    private CartItemMenuListener itemMenuListener;

    public CartItem2Adapter(CartItemMenuListener listener) {
        this.itemMenuListener = listener;
    }

    @Override
    public void loadItems(Collection<? extends CartItem> cartItems) {
        super.loadItems(cartItems);
    }

    @Override
    protected CartItemViewHolder onCreateViewHolder(ViewGroup parent, int itemType) {
        return new CartItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cart, parent, false));
    }

    @Override
    protected void onBindViewHolder(CartItemViewHolder viewHolder, CartItem cartItem, int
            position) {
        if (cartItem != null) {
            viewHolder.cartItemView.setCartItem(cartItem);
            if (itemMenuListener != null) {
                viewHolder.cartItemView.setCartItemMenuListener(itemMenuListener);
            }
        }
    }
}
