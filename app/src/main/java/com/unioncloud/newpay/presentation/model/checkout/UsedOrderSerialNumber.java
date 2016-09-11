package com.unioncloud.newpay.presentation.model.checkout;

import com.raizlabs.coreutils.events.Event;

/**
 * Created by cwj on 16/8/11.
 */
public class UsedOrderSerialNumber {

    private Event<UsedOrderSerialNumber> dataChangedEvent = new Event<>();

    private String serialNumber;

    public Event<UsedOrderSerialNumber> getDataChangedEvent() {
        return this.dataChangedEvent;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        getDataChangedEvent().raiseEvent(this);
    }

    public String getSerialNumber() {
        return serialNumber;
    }
}
