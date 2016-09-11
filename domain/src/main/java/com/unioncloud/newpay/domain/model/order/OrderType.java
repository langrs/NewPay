package com.unioncloud.newpay.domain.model.order;

/**
 * Created by cwj on 16/8/10.
 */
public enum  OrderType {
    SALE("销售单", "01"),
    REFUND("退货单", "02");

    String name;
    String value;

    OrderType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
