package com.unioncloud.newpay.data.entity.vip;

/**
 * Created by cwj on 16/8/17.
 */
public class VipEntity {

    private String ckcode;          // 会员卡号
    private String lmcode;          // 联名卡号
    private String cust_mobill;     // 手机号
    private String cust_name;       // 顾客名称
    private String cust_sex;        // 性别
    private String cust_addr;       // 顾客地址
    private String cust_birthday;   // 顾客生日
    private String cust_id;         // 顾客身份证
    private String cardjf;          // 卡积分
    private String cardtype;        // 卡类型
    private String cardstatus;      // 卡状态

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

    public String getCust_sex() {
        return cust_sex;
    }

    public void setCust_sex(String cust_sex) {
        this.cust_sex = cust_sex;
    }

    public String getCust_addr() {
        return cust_addr;
    }

    public void setCust_addr(String cust_addr) {
        this.cust_addr = cust_addr;
    }

    public String getCust_birthday() {
        return cust_birthday;
    }

    public void setCust_birthday(String cust_birthday) {
        this.cust_birthday = cust_birthday;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
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
}
