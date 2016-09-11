package com.unioncloud.newpay.data.entity.checkout;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * (预结算)订单
 */
public class CheckoutOrderEntity {

    @SerializedName("headInfo")
    private CheckoutHeaderEntity header;
    @SerializedName("itemInfo")
    private List<CheckoutItemEntity> itemList;

    public CheckoutHeaderEntity getHeader() {
        return header;
    }

    public void setHeader(CheckoutHeaderEntity header) {
        this.header = header;
    }

    public List<CheckoutItemEntity> getItemList() {
        return itemList;
    }

    public void setItemList(List<CheckoutItemEntity> itemList) {
        this.itemList = itemList;
    }

}
