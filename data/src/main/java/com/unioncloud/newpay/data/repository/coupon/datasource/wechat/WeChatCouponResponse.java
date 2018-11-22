package com.unioncloud.newpay.data.repository.coupon.datasource.wechat;

public class WeChatCouponResponse {
    private int errcode;
    private String errmsg;
    private String openid;
    private boolean can_consume;
    private String user_card_status;
    private String access_token;
    private int expires_in;
    private Card card;
//    这个是卡面值,从卡详情查询获取并后赋值的
    private Integer reduce_cost;

    public Integer getReduce_cost() {
        return reduce_cost;
    }

    public void setReduce_cost(Integer reduce_cost) {
        this.reduce_cost = reduce_cost;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public boolean isCan_consume() {
        return can_consume;
    }

    public void setCan_consume(boolean can_consume) {
        this.can_consume = can_consume;
    }

    public String getUser_card_status() {
        return user_card_status;
    }

    public void setUser_card_status(String user_card_status) {
        this.user_card_status = user_card_status;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}