package com.unioncloud.newpay.data.entity.order;

/**
 * Created by cwj on 16/8/16.
 */
public class SaleItemEntity {
    private String itemId;          // 商品ID
    private String itemNo;          // 商品条码
    private String rowNo;
    private String saleNum;
    private String salePrice;       // 原售价
    private String allDistAmt;      // (单项)优惠总金额
    private String itemSaleAmt;     // (单项)销售总金额
    private String salePoints;      // (单项)销售积分
    private String vipDisc;         // 会员打折率
    private String vipDiscAmt;      // 会员打折金额
    private String promDisc;        // 促销打折率
    private String promDiscAmt;     // 促销折扣金额

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getRowNo() {
        return rowNo;
    }

    public void setRowNo(String rowNo) {
        this.rowNo = rowNo;
    }

    public String getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(String saleNum) {
        this.saleNum = saleNum;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getAllDistAmt() {
        return allDistAmt;
    }

    public void setAllDistAmt(String allDistAmt) {
        this.allDistAmt = allDistAmt;
    }

    public String getItemSaleAmt() {
        return itemSaleAmt;
    }

    public void setItemSaleAmt(String itemSaleAmt) {
        this.itemSaleAmt = itemSaleAmt;
    }

    public String getSalePoints() {
        return salePoints;
    }

    public void setSalePoints(String salePoints) {
        this.salePoints = salePoints;
    }

    public String getVipDisc() {
        return vipDisc;
    }

    public void setVipDisc(String vipDisc) {
        this.vipDisc = vipDisc;
    }

    public String getVipDiscAmt() {
        return vipDiscAmt;
    }

    public void setVipDiscAmt(String vipDiscAmt) {
        this.vipDiscAmt = vipDiscAmt;
    }

    public String getPromDisc() {
        return promDisc;
    }

    public void setPromDisc(String promDisc) {
        this.promDisc = promDisc;
    }

    public String getPromDiscAmt() {
        return promDiscAmt;
    }

    public void setPromDiscAmt(String promDiscAmt) {
        this.promDiscAmt = promDiscAmt;
    }
}
