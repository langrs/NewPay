package com.unioncloud.newpay.data.entity.coupon;

/**
 * Created by cwj on 16/9/13.
 */
public class VipPointRebateEntity {

    private String ckcode;          // 会员卡号
    private String lmcode;          // 联名卡号
    private String cust_mobill;     // 手机号
    private String cust_name;       // 顾客名称

    private String cardjf;          // 积分
    private String cardtype;        // 卡类型
    private String cardstatus;      // 卡状态
    private String kjjf;            // 扣减积分
    private String flje;            // 返利金额

    public String getCkcode() {
        return ckcode;
    }

    public void setCkcode(String ckcode) {
        this.ckcode = ckcode;
    }

    public String getLmcode() {
        return lmcode;
    }

    public void setLmcode(String lmcode) {
        this.lmcode = lmcode;
    }

    public String getCust_mobill() {
        return cust_mobill;
    }

    public void setCust_mobill(String cust_mobill) {
        this.cust_mobill = cust_mobill;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCardjf() {
        return cardjf;
    }

    public void setCardjf(String cardjf) {
        this.cardjf = cardjf;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getCardstatus() {
        return cardstatus;
    }

    public void setCardstatus(String cardstatus) {
        this.cardstatus = cardstatus;
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
