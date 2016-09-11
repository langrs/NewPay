package com.unioncloud.newpay.domain.model.erp;

import java.io.Serializable;

/**
 * Created by cwj on 16/8/8.
 */
public class GiftCard implements Serializable {

    private String cardNumber;
    private String amountValue;
    private String cardType;
    private String cardStatus;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAmountValue() {
        return amountValue;
    }

    public void setAmountValue(String amountValue) {
        this.amountValue = amountValue;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }
}
