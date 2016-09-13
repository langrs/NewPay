package com.unioncloud.newpay.data.entity.coupon;

/**
 * Created by cwj on 16/9/13.
 */
public class CreatePointCouonCommand {
    private String ckcode;      // 卡号
    private String cardtype;    // 卡类型
    private String cardjf;      // 卡积分
    private String userNo;      // 操作人员
    private String kjjf;        // 扣减积分
    private String flje;        // 返利金额

    public String getCkcode() {
        return ckcode;
    }

    public void setCkcode(String ckcode) {
        this.ckcode = ckcode;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getCardjf() {
        return cardjf;
    }

    public void setCardjf(String cardjf) {
        this.cardjf = cardjf;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getKjjf() {
        return kjjf;
    }

    public void setKjjf(String kjjf) {
        this.kjjf = kjjf;
    }

    public String getFlje() {
        return flje;
    }

    public void setFlje(String flje) {
        this.flje = flje;
    }
}
