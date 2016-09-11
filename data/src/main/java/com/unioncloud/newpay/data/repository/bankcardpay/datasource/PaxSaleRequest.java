package com.unioncloud.newpay.data.repository.bankcardpay.datasource;

import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleRequest;

/**
 * Created by cwj on 16/8/17.
 */
public class PaxSaleRequest extends PaxAbsRequest {

    private String transAmount;
    /** 消费小票的类型 */
    private String ticketType;

    public PaxSaleRequest(BankcardSaleRequest request) {
        super();
        this.setTicketType(request.getTicketType());
        this.setTransAmount(String.valueOf(request.getTransAmount()));
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

}
