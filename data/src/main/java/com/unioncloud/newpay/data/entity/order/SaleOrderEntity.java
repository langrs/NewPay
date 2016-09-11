package com.unioncloud.newpay.data.entity.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cwj on 16/8/16.
 */
public class SaleOrderEntity {
//    @SerializedName("tSale")
    private SaleHeadEntity saleHead;
//    @SerializedName("saleDlist")
    private List<SaleItemEntity> saleDetail;
//    @SerializedName("salePayList")
    private List<SalePayEntity> salePay;

    public SaleHeadEntity getSaleHead() {
        return saleHead;
    }

    public void setSaleHead(SaleHeadEntity saleHead) {
        this.saleHead = saleHead;
    }

    public List<SaleItemEntity> getSaleDetail() {
        return saleDetail;
    }

    public void setSaleDetail(List<SaleItemEntity> saleDetail) {
        this.saleDetail = saleDetail;
    }

    public List<SalePayEntity> getSalePay() {
        return salePay;
    }

    public void setSalePay(List<SalePayEntity> salePay) {
        this.salePay = salePay;
    }
}
