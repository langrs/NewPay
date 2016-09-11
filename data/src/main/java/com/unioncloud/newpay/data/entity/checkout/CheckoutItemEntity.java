package com.unioncloud.newpay.data.entity.checkout;

/**
 * Created by cwj on 16/8/15.
 */
public class CheckoutItemEntity {

    private String storeId;
    private String itemId;
    private String rowNo;
    private String saleNum;
    private String salePrice;           // 原销售单价
    private String salePriceReal;       // 折后的实际销售单价
    private String allDistAmt;
    private String itemSaleAmt;         // 实销总额
    private String promInfo;            // 优惠信息
    private String vipDisc;             // 会员打折率
    private String vipDiscAmt;          // 会员打折金额
    private String promDisc;            // 促销打折率
    private String promDiscAmt;         // 促销打折金额
    private String reduceAmt;           // 满减优惠金额
    private String points;              // 销售积分
    private String couponDisc;          // 买换抵扣金额

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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

    public String getSalePriceReal() {
        return salePriceReal;
    }

    public void setSalePriceReal(String salePriceReal) {
        this.salePriceReal = salePriceReal;
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

    public String getPromInfo() {
        return promInfo;
    }

    public void setPromInfo(String promInfo) {
        this.promInfo = promInfo;
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

    public String getReduceAmt() {
        return reduceAmt;
    }

    public void setReduceAmt(String reduceAmt) {
        this.reduceAmt = reduceAmt;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getCouponDisc() {
        return couponDisc;
    }

    public void setCouponDisc(String couponDisc) {
        this.couponDisc = couponDisc;
    }
}
