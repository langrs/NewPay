package com.unioncloud.newpay.presentation.presenter.checkout;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.order.SaleOrderInteractor;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.erp.VipCard;
import com.unioncloud.newpay.domain.model.order.OrderType;
import com.unioncloud.newpay.domain.model.order.SaleOrder;
import com.unioncloud.newpay.domain.model.order.SaleOrderHeader;
import com.unioncloud.newpay.domain.model.order.SaleOrderResult;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.utils.DateFormatUtils;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.presentation.model.checkout.OrderTotals;
import com.unioncloud.newpay.presentation.model.checkout.SelectedVipCard;
import com.unioncloud.newpay.presentation.model.payment.UsedPayments;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import java.util.Date;
import java.util.List;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/16.
 */
public class SubmitOrderHandler extends UpdateHandler<SaleOrderResult, SubmitOrderHandler>
        implements Runnable {

    PosInfo posInfo;
    String orderId;
    OrderType orderType;
    OrderTotals orderTotals;
    SelectedVipCard selectedVipCard;
    List<CartItem> cartItems;
    UsedPayments usedPayments;

    public SubmitOrderHandler(PosInfo posInfo, String orderId, OrderType orderType,
                              SelectedVipCard selectedVipCard,
                              OrderTotals orderTotals,
                              List<CartItem> cartItems,
                              UsedPayments usedPayments) {
        super(null, true);
        this.posInfo = posInfo;
        this.orderId = orderId;
        this.orderType = orderType;
        this.selectedVipCard = selectedVipCard;
        this.orderTotals = orderTotals;
        this.cartItems = cartItems;
        this.usedPayments = usedPayments;
    }

    @Override
    public void run() {
        SaleOrderInteractor interactor = new SaleOrderInteractor(
                PresenterUtils.getExecutorProvider(),
                createSaleOrder(),
                PresenterUtils.getSaleOrderRepository());
        interactor.execute(new Subscriber<SaleOrderResult>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(SaleOrderResult saleOrderResult) {
                data = saleOrderResult;
                onUpdateCompleted();
            }
        });
    }

    private SaleOrder createSaleOrder() {
        SaleOrderHeader header = new SaleOrderHeader();
        header.setCompanyId(posInfo.getCompanyId());
        header.setShopId(posInfo.getShopId());
        header.setStoreId(posInfo.getStoreId());
        header.setPosId(posInfo.getPosId());
        header.setUserId(posInfo.getUserId());
        header.setSaleNumber(orderId);
        header.setSaleDate(DateFormatUtils.yyyyMMddHHmmss(new Date()));
        header.setDealType(orderType.getValue());
        header.setSaleType("01");
        VipCard vipCard = selectedVipCard.getVipCard();
        if (vipCard != null) {
            header.setVipCardNumber(selectedVipCard.getVipCard().getCardNumber());
            header.setPreviousPoints(vipCard.getPoints());
        }
        header.setSalePoints(String.valueOf(orderTotals.salePoints));
        header.setOriginalAmount(MoneyUtils.fenToString(orderTotals.subtotal));
        header.setSaleAmount(MoneyUtils.fenToString(orderTotals.total));
        header.setPayAmount(MoneyUtils.fenToString(usedPayments.getPaidTotal()));
        header.setDiscountAmount(MoneyUtils.fenToString(orderTotals.discount));
        header.setVipDiscountAmount(MoneyUtils.fenToString(orderTotals.vipDiscount));
        header.setIsTrain("");
        header.setRefundAmount("0");
        header.setEbillType("01");

        int changedAmount = 0;
        int excessAmount = 0;
        for (PaymentUsed used : usedPayments.getUsedList()) {
            changedAmount += used.getChangeAmount();
            excessAmount += used.getExcessAmount();
        }
        header.setChangedAmount(MoneyUtils.fenToString(changedAmount));
        header.setExcessAmount(MoneyUtils.fenToString(excessAmount));

        SaleOrder order = new SaleOrder();
        order.setHeader(header);
        order.setPaymentUsedList(usedPayments.getUsedList());

        order.setItemList(cartItems);
        return order;
    }

}
