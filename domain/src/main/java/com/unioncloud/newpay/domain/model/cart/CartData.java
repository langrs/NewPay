package com.unioncloud.newpay.domain.model.cart;

import com.unioncloud.newpay.domain.model.erp.VipCard;

import java.util.List;

/**
 * Created by cwj on 16/8/10.
 */
public class CartData {
    private List<CartItem> cartItems;
    private VipCard vipCard;

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public VipCard getVipCard() {
        return vipCard;
    }

    public void setVipCard(VipCard vipCard) {
        this.vipCard = vipCard;
    }
}
