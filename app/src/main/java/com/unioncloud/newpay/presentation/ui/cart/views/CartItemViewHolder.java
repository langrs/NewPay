package com.unioncloud.newpay.presentation.ui.cart.views;

import android.view.View;

import com.raizlabs.universaladapter.ViewHolder;
import com.unioncloud.newpay.R;

/**
 * Created by cwj on 16/8/14.
 */
public class CartItemViewHolder extends ViewHolder {

    public CartItemView cartItemView;

    public CartItemViewHolder(View itemView) {
        super(itemView);
        bindView(itemView);
    }

    public void bindView(View view) {
        if (view != null) {
            cartItemView = ((CartItemView)view.findViewById(R.id.list_item_cart_product));
        }
    }

}
