package com.unioncloud.newpay.presentation.model.checkout;

import android.text.TextUtils;

import com.raizlabs.coreutils.events.Event;
import com.raizlabs.coreutils.functions.Delegate;
import com.unioncloud.newpay.NewPayApplication;
import com.unioncloud.newpay.domain.model.cart.AddProductEntry;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.order.OrderType;
import com.unioncloud.newpay.domain.model.order.SaleOrder;
import com.unioncloud.newpay.domain.model.order.SaleOrderHeader;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.utils.DateFormatUtils;
import com.unioncloud.newpay.presentation.model.cart.CartDataManager;
import com.unioncloud.newpay.presentation.model.payment.UsedPayments;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.cart.AddCartItemsHandler;
import com.unioncloud.newpay.presentation.presenter.cart.UpdateItemPriceHandler;
import com.unioncloud.newpay.presentation.presenter.cart.UpdateItemQuantityHandler;
import com.unioncloud.newpay.presentation.presenter.checkout.EmptyCheckoutHandler;
import com.unioncloud.newpay.presentation.presenter.cart.RemoveCartItemsHandler;
import com.unioncloud.newpay.presentation.presenter.checkout.CalculateAmountsHandler;
import com.unioncloud.newpay.presentation.presenter.checkout.GetSnAndCalcuateHandler;
import com.unioncloud.newpay.presentation.presenter.checkout.SubmitOrderHandler;
import com.unioncloud.newpay.presentation.presenter.refund.RefundOrderHandler;
import com.unioncloud.newpay.presentation.presenter.sharedpreferences.PreferencesUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 与订单生成相关的管理类
 */
public class CheckoutDataManager {
    private static CheckoutDataManager instance;

    private SelectedVipCard selectedVipCard = new SelectedVipCard();
    private SelectedOrderType selectedOrderType = new SelectedOrderType();
    private String lastSerialNumber = null;
    private OrderTotals orderTotals = new OrderTotals();
    private UsedPayments usedPayments = new UsedPayments();

    public static CheckoutDataManager getInstance() {
        if (instance == null) {
            instance = new CheckoutDataManager();
        }
        return instance;
    }

    public UpdateItemPriceHandler updateItemPrice(CartItem cartItem, int newPrice) {
        UpdateItemPriceHandler handler = new UpdateItemPriceHandler(cartItem, newPrice);
        handler.run();
        return handler;
    }

    public UpdateItemQuantityHandler updateItemQuantity(CartItem cartItem, int newQuantity) {
        UpdateItemQuantityHandler handler = new UpdateItemQuantityHandler(cartItem, newQuantity);

        return handler;
    }

    // 添加订单项
    public AddCartItemsHandler addCartItem(AddProductEntry entry) {
        AddCartItemsHandler handler = new AddCartItemsHandler(entry);
        handler.run();
        return handler;
    }

    // 生成订单的销售策略计算金额
    public CalculateAmountsHandler calculateAmounts() {
        CalculateAmountsHandler handler;
        handler = new CalculateAmountsHandler(orderTotals, selectedOrderType,
                getCartDataManager().getItemList(), selectedVipCard, getPosInfo());
        handler.setOrderId(createOrderId());
//        handler = new GetSnAndCalcuateHandler(orderTotals, selectedOrderType,
//                getCartDataManager().getItemList(),
//                selectedVipCard, getPosInfo());
        handler.run();
        return handler;
    }

    public SelectedVipCard getSelectedVipCard() {
        return selectedVipCard;
    }

    public SelectedOrderType getSelectedOrderType() {
        return selectedOrderType;
    }

    public OrderTotals getOrderTotals() {
        return this.orderTotals;
    }

    // 预结算完成
    public void onCalculatedTotals(List<CartItem> list) {
        CartDataManager.getInstance().updateLocalSaleStrategy(list);
        orderTotals.calculateTotals(getCartDataManager().getItemList());
        usedPayments.clear();
    }

    public int getPaidTotal() {
        return usedPayments.getPaidTotal();
    }

    // 订单获取应付金额
    public int getNeedPayTotal() {
        return orderTotals.total;
    }

    // 订单未支付的金额
    public int getUnpayTotal() {
        return orderTotals.total - usedPayments.getPaidTotal();
    }

    public PosInfo getPosInfo() {
        return PosDataManager.getInstance().getPosInfo();
    }

    public UsedPayments getUsedPayments() {
        return usedPayments;
    }

    public void usePayment(PaymentUsed used) {
        if (used != null) {
            if (usedPayments.getUsedList().isEmpty()) {
                saveLastOrderSerialNumber();
            }
            usedPayments.usedPayment(used);
        }
    }

    public void addPaidListener(Delegate<UsedPayments> listener) {
        usedPayments.getDataChangedEvent().addListener(listener);
    }

    public void removePaidListener(Delegate<UsedPayments> listener) {
        usedPayments.getDataChangedEvent().removeListener(listener);
    }

    public CartDataManager getCartDataManager() {
        return CartDataManager.getInstance();
    }

    public boolean hasOrderType() {
        return selectedOrderType.getOrderType() != null;
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

    // 移除服务器订单中的一项.实际上是直接操作的购物车
    public RemoveCartItemsHandler removeCartItem(CartItem cartItem) {
        RemoveCartItemsHandler response = new RemoveCartItemsHandler(cartItem);
        return response;
    }

    public SubmitOrderHandler submitOrder() {
        SubmitOrderHandler handler = new SubmitOrderHandler(
                getPosInfo(), createOrderId(),
                selectedOrderType.getOrderType(),
                selectedVipCard,
                orderTotals,
                getCartDataManager().getItemList(),
                usedPayments
        );
        return handler;
    }

    // 清空服务器订单数据
    public EmptyCheckoutHandler emptyCheckout() {
        EmptyCheckoutHandler handler = new EmptyCheckoutHandler(true);
        handler.run();
        return handler;
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

    // 清空,释放资源,
    public void clear() {
        lastSerialNumber = null;
        selectedOrderType.setOrderType(null);
        selectedVipCard.clear();
        orderTotals.clear();
        usedPayments.clear();
        getCartDataManager().clear();
    }

    public void saveLastOrderSerialNumber() {
        PreferencesUtils.saveLastOrderSerialNumber(NewPayApplication.getInstance(), lastSerialNumber);
    }

    public void finishOrder() {
        lastSerialNumber = null;
        selectedOrderType.setOrderType(null);
        selectedVipCard.clear();
        orderTotals.clear();
        usedPayments.clear();
    }

}
