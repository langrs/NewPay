package com.unioncloud.newpay.domain.model.payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cwj on 16/7/1.
 */
public class PaymentMode {

    private String parentId;
    private String paymentModeId;
    private String paymentModeNumber;
    private String paymentModeName;

    private String paymentId;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(String paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    public String getPaymentModeNumber() {
        return paymentModeNumber;
    }

    public void setPaymentModeNumber(String paymentModeNumber) {
        this.paymentModeNumber = paymentModeNumber;
    }

    public String getPaymentModeName() {
        return paymentModeName;
    }

    public void setPaymentModeName(String paymentModeName) {
        this.paymentModeName = paymentModeName;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

}
