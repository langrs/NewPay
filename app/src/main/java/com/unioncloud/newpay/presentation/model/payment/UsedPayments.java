package com.unioncloud.newpay.presentation.model.payment;

import com.raizlabs.coreutils.events.Event;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 16/8/15.
 */
public class UsedPayments {
    private Event<UsedPayments> dataChanged = new Event<>();

    ArrayList<PaymentUsed> usedList = new ArrayList<>();

    public Event<UsedPayments> getDataChangedEvent() {
        return dataChanged;
    }

    public void usedPayment(PaymentUsed usedInfo) {
        if (usedInfo != null) {
            synchronized (usedList) {
                usedInfo.setRowNo(usedList.size() + 1);
                usedList.add(usedInfo);
                getDataChangedEvent().raiseEvent(this);
            }
        }
    }

    public List<PaymentUsed> getUsedList() {
        return usedList;
    }

    public int getPaidTotal() {
        synchronized (usedList) {
            int total = 0;
            for (PaymentUsed used : usedList) {
                total += used.getPayAmount();
            }
            return total;
        }
    }

    public boolean hasUsed(Payment payment) {
        for (PaymentUsed used : usedList) {
            if (used.getPaymentId().equals(payment.getPaymentId())) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        synchronized (usedList) {
            usedList.clear();
            getDataChangedEvent().raiseEvent(this);
        }
    }
}
