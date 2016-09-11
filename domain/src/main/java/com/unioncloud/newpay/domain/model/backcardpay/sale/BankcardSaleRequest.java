package com.unioncloud.newpay.domain.model.backcardpay.sale;

/**
 * Created by cwj on 16/7/19.
 */
public class BankcardSaleRequest {
    /** 交易金额 */
    private int transAmount;
    /** 消费小票的类型 */
    private String ticketType;

    public BankcardSaleRequest() {
    }

    public int getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(int transAmount) {
        this.transAmount = transAmount;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }


}
