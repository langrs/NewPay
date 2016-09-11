package com.unioncloud.newpay.domain.model.product;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

/**
 * Created by cwj on 16/3/28.
 */
public class Product implements Serializable {

    private String companyId;

    private String productId;
    private String productNumber;
    private String productName;

    private String shopId;
    private String storeId;
    private String innerNumber; // 销售内码
    private String isPrice;    // 是否定价
    private String attr;        // 商品属性
    private int price;       // 价格
    private String brandId;     // 品牌
    private String categoryId;  // 品类
    private String isFlag;     // 是否有效

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getInnerNumber() {
        return innerNumber;
    }

    public void setInnerNumber(String innerNumber) {
        this.innerNumber = innerNumber;
    }

    public String getIsPrice() {
        return isPrice;
    }

    public void setIsPrice(String isPrice) {
        this.isPrice = isPrice;
    }

    public boolean isFixPrice() {
        return "1".equals(isPrice);
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getIsFlag() {
        return isFlag;
    }

    public void setIsFlag(String isFlag) {
        this.isFlag = isFlag;
    }

    public static String[] getCategories(List<Product> list) {
        HashSet<String> categories = new HashSet<>();
        for (Product product : list) {
            categories.add(product.categoryId);
        }
        return (String[]) categories.toArray();
    }
}
