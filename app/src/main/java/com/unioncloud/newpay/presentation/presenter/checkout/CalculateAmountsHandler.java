package com.unioncloud.newpay.presentation.presenter.checkout;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.checkout.CalculateAmountsInteractor;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.presentation.model.cart.CartDataManager;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.checkout.OrderTotals;
import com.unioncloud.newpay.presentation.model.checkout.SelectedOrderType;
import com.unioncloud.newpay.presentation.model.checkout.SelectedVipCard;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import java.util.List;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/10.
 */
public class CalculateAmountsHandler extends UpdateHandler<OrderTotals, CalculateAmountsHandler>
        implements Runnable {

    protected PosInfo posInfo;
    private List<CartItem> itemList;
    protected String orderId;
    private SelectedOrderType selectedOrderType;
    private SelectedVipCard selectedVipCard;

    public CalculateAmountsHandler(OrderTotals data,
                                   SelectedOrderType selectedOrderType,
                                   List<CartItem> itemList,
                                   SelectedVipCard selectedVipCard,
                                   PosInfo posInfo) {
        super(data, true);
        this.selectedOrderType = selectedOrderType;
        this.itemList = itemList;
        this.selectedVipCard = selectedVipCard;
        this.posInfo = posInfo;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public void run() {
        CalculateAmountsInteractor interactor = new CalculateAmountsInteractor(
                PresenterUtils.getExecutorProvider(),
                orderId, posInfo, selectedVipCard.getVipCard(),
                selectedOrderType.getOrderType(), itemList,
                PresenterUtils.getCheckoutRepository());

        interactor.execute(new Subscriber<List<CartItem>>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(List<CartItem> cartItems) {
                CheckoutDataManager.getInstance().onCalculatedTotals(cartItems);
                data = CheckoutDataManager.getInstance().getOrderTotals();
                onUpdateCompleted();
            }
        });
    }

}
