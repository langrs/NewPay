package com.unioncloud.newpay.presentation.model.refund;

import android.text.TextUtils;

import com.raizlabs.coreutils.events.Event;
import com.raizlabs.coreutils.events.HandlerEvent;
import com.raizlabs.coreutils.events.IEvent;
import com.raizlabs.coreutils.util.observable.lists.ObservableList;
import com.raizlabs.coreutils.util.observable.lists.ObservableListWrapper;
import com.unioncloud.newpay.NewPayApplication;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.order.SaleOrder;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.utils.DateFormatUtils;
import com.unioncloud.newpay.presentation.model.checkout.OrderTotals;
import com.unioncloud.newpay.presentation.model.payment.UsedPayments;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.sharedpreferences.PreferencesUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by cwj on 16/8/26.
 */
public class RefundDataManager {

    private static RefundDataManager instance;

    public static RefundDataManager getInstance() {
        if (instance == null) {
            instance = new RefundDataManager();
        }
        return instance;
    }

    private UsedPayments usedPayments = new UsedPayments();
    private String lastSerialNumber = null;
    private ObservableListWrapper<CartItem> itemList = new ObservableListWrapper<>();
    private IEvent<RefundDataManager> itemChanged = new Event<>();
    private OrderTotals orderTotals = new OrderTotals();

    //    退货授权人
    private String authorizer;

    public void usePayment(PaymentUsed used) {
        if (used != null) {
            if (usedPayments.getUsedList().isEmpty()) {
                saveLastOrderSerialNumber();
            }
            usedPayments.usedPayment(used);
        }
    }

    public String createOrderId() {
        if (TextUtils.isEmpty(lastSerialNumber)) {
            boolean hasLocalSn = getLocalSerialNumber();
            if (!hasLocalSn) {
                return null;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getPosInfo().getShopNumber())
                .append(DateFormatUtils.yyyyMMdd(new Date()))
                .append(getPosInfo().getPosNumber())
                .append(lastSerialNumber);
        return sb.toString();
    }

    public PosInfo getPosInfo() {
        return PosDataManager.getInstance().getPosInfo();
    }

    public List<PaymentUsed> getPaidPayments() {
        return usedPayments.getUsedList();
    }

    public void setLastSerialNumber(String lastSerialNumber) {
        this.lastSerialNumber = lastSerialNumber;
    }

    private boolean getLocalSerialNumber() {
        String orderSerialNumber = PreferencesUtils.getOrderSerialNumber(
                NewPayApplication.getInstance());
        if (!TextUtils.isEmpty(orderSerialNumber)) {
            setLastSerialNumber(orderSerialNumber);
            return true;
        }
        return false;
    }

    public void saveLastOrderSerialNumber() {
        PreferencesUtils.saveLastOrderSerialNumber(NewPayApplication.getInstance(), lastSerialNumber);
    }

    public ObservableList<CartItem> getItemList() {
        return itemList;
    }

    public void addLocalItems(List<CartItem> cartItems) {
        if (cartItems != null && cartItems.size() > 0) {
            for (CartItem cartItem: cartItems) {
                itemList.add(cartItem);
            }
            notifyCartItemChanged();
        }
    }

    public void notifyCartItemChanged() {
        itemChanged.raiseEvent(this);
    }

    public void clear() {
        usedPayments.clear();
        lastSerialNumber = null;
        orderTotals.clear();
        itemList.clear();
        itemChanged.raiseEvent(this);
        authorizer = null;
    }

    public String getAuthorizer() {
        return authorizer;
    }

    public void setAuthorizer(String authorizer) {
        this.authorizer = authorizer;
    }

}
