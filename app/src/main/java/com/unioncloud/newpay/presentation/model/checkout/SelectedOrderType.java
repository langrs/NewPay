package com.unioncloud.newpay.presentation.model.checkout;

import com.raizlabs.coreutils.events.Event;
import com.unioncloud.newpay.domain.model.order.OrderType;

/**
 * Created by cwj on 16/8/11.
 */
public class SelectedOrderType {
    private Event<SelectedOrderType> dataChangedEvent = new Event<>();
    private OrderType orderType;

    public Event<SelectedOrderType> getDataChangedEvent() {
        return this.dataChangedEvent;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
        dataChangedEvent.raiseEvent(this);
    }

}
