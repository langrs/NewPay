package com.unioncloud.newpay.presentation.ui.refund;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.model.refund.RefundDataManager;
import com.unioncloud.newpay.presentation.ui.pay.PaymentSignpost;

/**
 * Created by cwj on 16/8/25.
 */
public class CashRefundFragment extends RefundFragment {

    public static CashRefundFragment newInstance() {
        CashRefundFragment fragment = new CashRefundFragment();
        return fragment;
    }

    @Override
    protected PaymentSignpost getPaymentSignpost() {
        return PaymentSignpost.CASH;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        originalContainerView.setVisibility(View.GONE);
    }

    @Override
    protected void refund() {
        Payment cash = PosDataManager.getInstance().getPaymentByNumberInt(
                PaymentSignpost.CASH.numberToInt());

        PaymentUsed used = new PaymentUsed();
        used.setPaymentId(cash.getPaymentId());
        used.setPaymentName(cash.getPaymentName());
        used.setPaymentQy(cash.getPaymentQy());
        used.setPayAmount(-getRefundAmount());
        used.setExcessAmount(0);
        used.setCurrencyId(cash.getCurrencyId());
        used.setExchangeRate(cash.getExchangeRate());
        used.setRelationNumber("");
        RefundDataManager.getInstance().usePayment(used);
        onRefundSuccess();
    }
}
