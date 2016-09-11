package com.unioncloud.newpay.domain.model.cart;

import com.unioncloud.newpay.domain.model.product.Product;

/**
 *
 */
public class AddProductEntry {
    int price;
    int quantity;
    Product product;

    public AddProductEntry(Product product, int quantity, int price) {
        this.price = price;
        this.quantity = quantity;
        this.product = product;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
