package com.unioncloud.newpay.domain.model.print;

import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.model.pos.PosInfo;

import java.util.List;

/**
 * Created by cwj on 16/8/19.
 */
public class PrintOrderInfo {
    private boolean isReprint;
    private PosInfo posInfo;
    private String orderId;
    private String total;
    private String paidTotal;
    private String discountTotal;
    private String vipNo;
//    private VipCard vipCard;
    private List<PaymentUsed> paymentUsedList;
    private List<CartItem> cartItemList;
//    private SaleOrderResult saleOrderResult;
    private String saleTime;
    private String salePoints;
    private String originalPoints;
    private String footer;

    private List<Coupon> coupons;

    public boolean isReprint() {
        return isReprint;
    }

    public void setReprint(boolean reprint) {
        isReprint = reprint;
    }

    public PosInfo getPosInfo() {
        return posInfo;
    }

    public void setPosInfo(PosInfo posInfo) {
        this.posInfo = posInfo;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPaidTotal() {
        return paidTotal;
    }

    public void setPaidTotal(String paidTotal) {
        this.paidTotal = paidTotal;
    }

    public String getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(String discountTotal) {
        this.discountTotal = discountTotal;
    }

//    public VipCard getVipCard() {
//        return vipCard;
//    }
//
//    public void setVipCard(VipCard vipCard) {
//        this.vipCard = vipCard;
//    }

    public String getVipNo() {
        return vipNo;
    }

    public void setVipNo(String vipNo) {
        this.vipNo = vipNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<PaymentUsed> getPaymentUsedList() {
        return paymentUsedList;
    }

    public void setPaymentUsedList(List<PaymentUsed> paymentUsedList) {
        this.paymentUsedList = paymentUsedList;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

//    public SaleOrderResult getSaleOrderResult() {
//        return saleOrderResult;
//    }
//
//    public void setSaleOrderResult(SaleOrderResult saleOrderResult) {
//        this.saleOrderResult = saleOrderResult;
//    }


    public String getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(String saleTime) {
        this.saleTime = saleTime;
    }

    public String getSalePoints() {
        return salePoints;
    }

    public void setSalePoints(String salePoints) {
        this.salePoints = salePoints;
    }

    public String getOriginalPoints() {
        return originalPoints;
    }

    public void setOriginalPoints(String originalPoints) {
        this.originalPoints = originalPoints;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }
}
