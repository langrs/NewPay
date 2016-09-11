package com.unioncloud.newpay.domain.model.order;

import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cwj on 16/7/1.
 */
public class SaleOrder implements Serializable {
    private SaleOrderHeader header;
    private List<CartItem> itemList;
    private List<PaymentUsed> paymentUsedList;

    public SaleOrderHeader getHeader() {
        return header;
    }

    public void setHeader(SaleOrderHeader header) {
        this.header = header;
    }

    public List<CartItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<CartItem> itemList) {
        this.itemList = itemList;
    }

    public List<PaymentUsed> getPaymentUsedList() {
        return paymentUsedList;
    }

    public void setPaymentUsedList(List<PaymentUsed> paymentUsedList) {
        this.paymentUsedList = paymentUsedList;
    }
}
