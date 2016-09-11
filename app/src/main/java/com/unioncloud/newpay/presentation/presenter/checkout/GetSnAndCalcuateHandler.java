package com.unioncloud.newpay.presentation.presenter.checkout;

import android.text.TextUtils;

import com.unioncloud.newpay.NewPayApplication;
import com.unioncloud.newpay.domain.interactor.checkout.GetOrderSerialNumberInteractor;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.checkout.OrderTotals;
import com.unioncloud.newpay.presentation.model.checkout.SelectedOrderType;
import com.unioncloud.newpay.presentation.model.checkout.SelectedVipCard;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;
import com.unioncloud.newpay.presentation.presenter.sharedpreferences.PreferencesUtils;

import java.util.List;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/15.
 */
public class GetSnAndCalcuateHandler extends CalculateAmountsHandler {

    public GetSnAndCalcuateHandler(OrderTotals data, SelectedOrderType selectedOrderType,
                                   List<CartItem> itemList, SelectedVipCard selectedVipCard,
                                   PosInfo posInfo) {
        super(data, selectedOrderType, itemList, selectedVipCard, posInfo);
    }

    @Override
    public void run() {
        if (!getLocalSerialNumber()) {
            getRemoteSerialNumber();
        }
    }

    private void getRemoteSerialNumber() {
        GetOrderSerialNumberInteractor interactor = new GetOrderSerialNumberInteractor(
                PresenterUtils.getExecutorProvider(),
                posInfo,
                PresenterUtils.getSaleOrderRepository());
        interactor.execute(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(String s) {
                calculateAmounts(s);
            }
        });
    }

    private void calculateAmounts(String orderSerialNumber) {
        CheckoutDataManager.getInstance().setLastSerialNumber(orderSerialNumber);
        orderId = CheckoutDataManager.getInstance().createOrderId();
        super.run();
    }

    private boolean getLocalSerialNumber() {
        String orderSerialNumber = PreferencesUtils.getOrderSerialNumber(
                NewPayApplication.getInstance());
        if (!TextUtils.isEmpty(orderSerialNumber)) {
            calculateAmounts(orderSerialNumber);
            return true;
        }
        return false;
    }
}
