package com.unioncloud.newpay.presentation.ui.refund;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.unioncloud.newpay.domain.model.erp.GiftCard;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.model.refund.RefundDataManager;
import com.unioncloud.newpay.presentation.ui.pay.PaymentSignpost;

/**
 * Created by cwj on 16/9/3.
 */
public class GiftCardRefundFragment extends RefundFragment {

    public static GiftCardRefundFragment newInstance(GiftCard giftCard) {
        GiftCardRefundFragment fragment = new GiftCardRefundFragment();
        fragment.setGiftCard(giftCard);
        return fragment;
    }

    public static GiftCardRefundFragment newInstance() {
        GiftCardRefundFragment fragment = new GiftCardRefundFragment();
        return fragment;
    }

    private void setGiftCard(GiftCard giftCard) {
        getArguments().putSerializable("giftCard", giftCard);
    }

    private GiftCard getUsingGiftCard() {
        return (GiftCard) getArguments().getSerializable("giftCard");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        originalContainerView.setVisibility(View.GONE);
    }

    @Override
    protected PaymentSignpost getPaymentSignpost() {
        return PaymentSignpost.GIFT;
    }

    @Override
    protected void refund() {
        Payment payment = PosDataManager.getInstance().getPaymentByNumberInt(
                PaymentSignpost.GIFT.numberToInt());

        PaymentUsed used = new PaymentUsed();
        used.setPaymentId(payment.getPaymentId());
        used.setPaymentName(payment.getPaymentName());
        used.setPaymentQy(payment.getPaymentQy());
        used.setPayAmount(-getRefundAmount());
        used.setExcessAmount(0);
        used.setCurrencyId(payment.getCurrencyId());
        used.setExchangeRate(payment.getExchangeRate());
//        used.setRelationNumber(getOriginalBillNo());
        used.setRelationNumber(getUsingGiftCard().getCardNumber());  // 使用查询到的储值卡而不是原来的储值卡
        RefundDataManager.getInstance().usePayment(used);
        onRefundSuccess();
    }
}
