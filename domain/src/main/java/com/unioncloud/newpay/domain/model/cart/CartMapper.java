package com.unioncloud.newpay.domain.model.cart;

/**
 * Created by cwj on 16/7/1.
 */
public class CartMapper {

    public static CartItem toFrom(AddProductEntry entry) {
        CartItem item = new CartItem();
        item.setStoreId(entry.product.getStoreId());
        item.setProductId(entry.product.getProductId());
        item.setProductName(entry.product.getProductName());
        item.setProductNumber(entry.product.getProductNumber());
        item.setSellUnitPrice(entry.price);
        item.setQuantity(entry.quantity);

        return item;
    }
}
