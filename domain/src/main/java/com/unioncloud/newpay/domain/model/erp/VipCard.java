package com.unioncloud.newpay.domain.model.erp;

/**
 * Created by cwj on 16/7/1.
 */
public class VipCard {
    private String cardNumber;       // 会员卡号
    private String coBrandedNumber; // 联名卡号
    private String phoneNumber;
    private String name;
    private String sex;
    private String address;
    private String birthday;
    private String idNumber;        // 身份证号
    private String points;          // 积分
    private String cardType;        // 卡类型
    private String cardStatus;      // 卡状态

    public VipCard() {
    }

    public VipCard(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public VipCard(VipCard vipCard) {
        this.cardNumber = vipCard.cardNumber;
        this.coBrandedNumber = vipCard.coBrandedNumber;
        this.phoneNumber = vipCard.phoneNumber;
        this.name = vipCard.name;
        this.sex = vipCard.sex;
        this.address = vipCard.address;
        this.birthday = vipCard.birthday;
        this.idNumber = vipCard.idNumber;
        this.points = vipCard.points;
        this.cardType = vipCard.cardType;
        this.cardStatus = vipCard.cardStatus;
    }

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
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
}
