package com.unioncloud.newpay.domain.model.payment;

/**
 * Created by cwj on 16/8/8.
 */
public class PrePayItem {
    private String storeId;
    private String productId;
    private int sellUnitPrice;
    private int quantity;
    private int rowNo;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getSellUnitPrice() {
        return sellUnitPrice;
    }

    public void setSellUnitPrice(int sellUnitPrice) {
        this.sellUnitPrice = sellUnitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }
}
