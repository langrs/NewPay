package com.unioncloud.newpay.presentation.model.checkout;

import android.text.TextUtils;

import com.raizlabs.coreutils.events.Event;
import com.unioncloud.newpay.domain.model.cart.CartItem;

import java.util.List;

/**
 * Created by cwj on 16/8/10.
 */
public class OrderTotals {
    private Event<OrderTotals> dataChangedEvent = new Event<>();

    public float salePoints;    // 销售总积分

    public int subtotal;   // 总计金额
    public int discount;   // 折扣金额
    public int total;      // 应付金额
    public int vipDiscount; // 会员折扣总额

    public Event<OrderTotals> getDataChangedEvent() {
        return this.dataChangedEvent;
    }

    public void calculateTotals(List<CartItem> itemList) {
        subtotal = 0;
        discount = 0;
        total = 0;
        salePoints = 0;
        vipDiscount = 0;
        for (CartItem item : itemList) {
            subtotal += item.getQuantity() * item.getSellUnitPrice();
            discount += item.getDiscountAmount();
            total += item.getSaleAmount();
            float points = TextUtils.isEmpty(item.getPoints()) ? 0 : Float.valueOf(item.getPoints());
            salePoints += points;
            vipDiscount += item.getVipDiscountAmount();
        }
        getDataChangedEvent().raiseEvent(this);
    }



    public void clear() {
        subtotal = 0;
        discount = 0;
        total = 0;
        salePoints = 0;
        vipDiscount = 0;
    }

}
