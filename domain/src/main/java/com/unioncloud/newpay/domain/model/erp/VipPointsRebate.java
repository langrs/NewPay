package com.unioncloud.newpay.domain.model.erp;

import java.io.Serializable;

/**
 * 会员积分返利
 * Created by cwj on 16/9/13.
 */
public class VipPointsRebate implements Serializable {
    private String cardNumber;       // 会员卡号
    private String coBrandedNumber; // 联名卡号
    private String phoneNumber;
    private String name;
    private String points;          // 积分
    private String cardType;        // 卡类型
    private String cardStatus;      // 卡状态
    private String rebatePoints;    // 可返利积分
    private String rebateAmount;    // 返利金额

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCoBrandedNumber() {
        return coBrandedNumber;
    }

    public void setCoBrandedNumber(String coBrandedNumber) {
        this.coBrandedNumber = coBrandedNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
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

    public String getRebatePoints() {
        return rebatePoints;
    }

    public void setRebatePoints(String rebatePoints) {
        this.rebatePoints = rebatePoints;
    }

    public String getRebateAmount() {
        return rebateAmount;
    }

    public void setRebateAmount(String rebateAmount) {
        this.rebateAmount = rebateAmount;
    }
}
