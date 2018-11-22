package com.unioncloud.newpay.data.repository.coupon.datasource.wechat;

public class WeChatCard {
    private String errcode;
    private String errmsg;
    private CardInfo card;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public CardInfo getCard() {
        return card;
    }

    public void setCard(CardInfo card) {
        this.card = card;
    }
}