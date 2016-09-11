package com.unioncloud.newpay.presentation.ui.cart.views;

import android.view.View;

import com.raizlabs.universaladapter.ViewHolder;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.cart.CartItem;

/**
 * Created by cwj on 16/8/14.
 */
public class CartItemHolderBinder {

    public final CartItem cartItem;
    public CartItemView itemView;


//    public CartItemHolderBinder(View itemView) {
//        super(itemView);
//    }

    public CartItemHolderBinder(CartItem cartItem) {
        this.cartItem = cartItem;
    }

    public void bindView(View view) {
        if (view != null) {
            this.itemView = ((CartItemView)view.findViewById(R.id.list_item_cart_product));
        }
    }

    public void bindData(CartItem cartItem) {
        if (cartItem != null) {
            itemView.setCartItem(cartItem);
        }
    }


}
