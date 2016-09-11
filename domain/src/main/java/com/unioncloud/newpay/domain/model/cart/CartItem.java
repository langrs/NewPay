package com.unioncloud.newpay.domain.model.cart;

import com.unioncloud.newpay.domain.model.product.Product;

import java.io.Serializable;

/**
 * Created by cwj on 16/6/29.
 */
public class CartItem extends Product implements Serializable {

    private int quantity;              // 数量
    private int sellUnitPrice;         // 商品销售单价
    private int rowNumber;             // 行号

    // 以下信息在预结算后返回
    private int sellUnitPriceReal;     // 折后的实际销售单价
    private int discountAmount;        // 折扣总额
    private int saleAmount;            // 实销总额
    private String promInfo;           // 优惠信息
    private int vipDiscount;           // 会员打折率
    private int vipDiscountAmount;     // 会员打折金额
    private int promDiscount;          // 促销打折率
    private int promDiscountAmount;    // 促销打折金额
    private int reduceAmount;          // 满减优惠金额
    private String points;             // 销售积分
    private int couponDiscountAmount;  // 买换折扣金额

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSellUnitPrice() {
        return sellUnitPrice;
    }

    public void setSellUnitPrice(int sellUnitPrice) {
        this.sellUnitPrice = sellUnitPrice;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }


    public int getSellUnitPriceReal() {
        return sellUnitPriceReal;
    }

    public void setSellUnitPriceReal(int sellUnitPriceReal) {
        this.sellUnitPriceReal = sellUnitPriceReal;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(int saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getPromInfo() {
        return promInfo;
    }

    public void setPromInfo(String promInfo) {
        this.promInfo = promInfo;
    }

    public int getVipDiscount() {
        return vipDiscount;
    }

    public void setVipDiscount(int vipDiscount) {
        this.vipDiscount = vipDiscount;
    }

    public int getVipDiscountAmount() {
        return vipDiscountAmount;
    }

    public void setVipDiscountAmount(int vipDiscountAmount) {
        this.vipDiscountAmount = vipDiscountAmount;
    }

    public int getPromDiscount() {
        return promDiscount;
    }

    public void setPromDiscount(int promDiscount) {
        this.promDiscount = promDiscount;
    }

    public int getPromDiscountAmount() {
        return promDiscountAmount;
    }

    public void setPromDiscountAmount(int promDiscountAmount) {
        this.promDiscountAmount = promDiscountAmount;
    }

    public int getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(int reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public int getCouponDiscountAmount() {
        return couponDiscountAmount;
    }

    public void setCouponDiscountAmount(int couponDiscountAmount) {
        this.couponDiscountAmount = couponDiscountAmount;
    }

    /** 根据预结算后返回的对象更新销售策略 */
    public void updateSaleStrategy(CartItem other) {
        if (other != null && getProductId().equals(other.getProductId())) {
            sellUnitPriceReal = other.getSellUnitPriceReal();
            discountAmount = other.getDiscountAmount();
            saleAmount = other.getSaleAmount();
            promInfo = other.promInfo;
            vipDiscount = other.getVipDiscount();
            vipDiscountAmount = other.getVipDiscountAmount();
            promDiscount = other.getPromDiscount();
            promDiscountAmount = other.getPromDiscountAmount();
            reduceAmount = other.getReduceAmount();
            points = other.getPoints();
            couponDiscountAmount = other.getCouponDiscountAmount();
        }
    }
}
