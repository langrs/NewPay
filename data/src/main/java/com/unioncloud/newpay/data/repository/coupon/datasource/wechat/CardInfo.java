package com.unioncloud.newpay.data.repository.coupon.datasource.wechat;

public class CardInfo {
    private String card_type;
    private CardCash cash;

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public CardCash getCash() {
        return cash;
    }

    public void setCash(CardCash cash) {
        this.cash = cash;
    }
}