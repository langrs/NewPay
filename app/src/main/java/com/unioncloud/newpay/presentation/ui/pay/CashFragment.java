package com.unioncloud.newpay.presentation.ui.pay;

import android.text.Html;

import com.esummer.android.dialog.DefaultDialogBuilder;
import com.esummer.android.dialog.UniversalDialog;
import com.raizlabs.coreutils.functions.Delegate;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.order.OrderType;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;

/**
 * Created by cwj on 16/8/16.
 */
public class CashFragment extends PayFragment {

    public static CashFragment newInstance() {
        CashFragment fragment = new CashFragment();
        return fragment;
    }

    @Override
    protected PaymentSignpost getPaymentSignpost() {
        return PaymentSignpost.CASH;
    }

    @Override
    protected void pay(int unpay, int willPay) {
        OrderType orderType = CheckoutDataManager.getInstance().getSelectedOrderType().getOrderType();
        if (orderType == OrderType.SALE) {
            if (willPay - unpay >= 10000) {
                showToast("现金找零金额不能超过100");
                return;
            }
        } else {
            if (willPay < unpay) {
                showToast("现金退款不能超过总金额");
                return;
            }
        }
        int payAmount = 0;
        int changeAmount = 0;
        if (orderType == OrderType.SALE) {
            payAmount = Math.min(unpay, willPay);
            changeAmount = Math.max(0, willPay - unpay);
        } else {
            payAmount = Math.max(unpay, willPay);
        }

        Payment cash = PosDataManager.getInstance().getPaymentByNumberInt(
                PaymentSignpost.CASH.numberToInt());

        PaymentUsed used = new PaymentUsed();
        used.setPaymentId(cash.getPaymentId());
        used.setPaymentName(cash.getPaymentName());
        used.setPaymentQy(cash.getPaymentQy());
        used.setPayAmount(payAmount);
        used.setChangeAmount(changeAmount);
        used.setCurrencyId(cash.getCurrencyId());
        used.setExchangeRate(cash.getExchangeRate());
        used.setRelationNumber("");

        CheckoutDataManager.getInstance().usePayment(used);
        if (changeAmount != 0) {
            showChangeAmount(changeAmount);
        } else {
            onPaidSuccess();
        }
    }

    private static final int DIALOG_CHANGE_AMOUNT_ID = 0X123;

    private void showChangeAmount(int changeAmount) {
        DefaultDialogBuilder builder = new DefaultDialogBuilder(DIALOG_CHANGE_AMOUNT_ID)
                .setTitle("找零")
                .setMessage(Html.fromHtml(getString(R.string.cash_change, MoneyUtils.fenToString(changeAmount))).toString())
                .setStyle(0);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(com.esummer.android.R.string.dialog_button_title_ok), new Delegate<UniversalDialog>() {
            @Override
            public void execute(UniversalDialog dialog) {
                onDialogOkClicked(dialog.getDialogId());
            }
        });
        saveDialogBuilder(builder);
        showDialog(builder);
    }

    @Override
    protected void onDialogOkClicked(int dialogId) {
        if (dialogId == DIALOG_CHANGE_AMOUNT_ID) {
            onPaidSuccess();
            return;
        }
        super.onDialogOkClicked(dialogId);
    }
}
