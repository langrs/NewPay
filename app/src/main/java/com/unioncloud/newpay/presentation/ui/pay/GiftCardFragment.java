package com.unioncloud.newpay.presentation.ui.pay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.erp.GiftCard;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;

/**
 * Created by cwj on 16/9/3.
 */
public class GiftCardFragment extends PayFragment {

    public static GiftCardFragment newInstance(GiftCard giftCard) {
        GiftCardFragment fragment = new GiftCardFragment();
        fragment.setGiftCard(giftCard);
        return fragment;
    }

    @Override
    protected PaymentSignpost getPaymentSignpost() {
        return PaymentSignpost.GIFT;
    }

    private void setGiftCard(GiftCard giftCard) {
        getArguments().putSerializable("giftCard", giftCard);
    }

    private GiftCard getUsingGiftCard() {
        return (GiftCard) getArguments().getSerializable("giftCard");
    }

    protected TextView giftValueTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pay_gift_card, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        giftValueTv = (TextView) view.findViewById(R.id.fragment_pay_giftCard_value);
        int giftValue = getUsingGiftCardValue();
        if (giftValue == 0) {
            showToast("储值卡余额为0!");
        } else {
            giftValueTv.setText(MoneyUtils.fenToString(giftValue));
        }
    }

    private int getUsingGiftCardValue() {
        GiftCard giftCard = getUsingGiftCard();
        int giftValue = MoneyUtils.getFen(giftCard.getAmountValue());
        return giftValue;
    }
    @Override
    protected void pay(int unpay, int willPay) {
        if (willPay > unpay) {
            showToast("支付金额超出未付款项");
            return;
        }
        int giftValue = getUsingGiftCardValue();
        if (giftValue < willPay) {
            showToast("储值卡余额不足!");
            return;
        }
        Payment cash = PosDataManager.getInstance().getPaymentByNumberInt(
                PaymentSignpost.GIFT.numberToInt());

        PaymentUsed used = new PaymentUsed();
        used.setPaymentId(cash.getPaymentId());
        used.setPaymentName(cash.getPaymentName());
        used.setPaymentQy(cash.getPaymentQy());
        used.setPayAmount(willPay);
        used.setCurrencyId(cash.getCurrencyId());
        used.setExchangeRate(cash.getExchangeRate());
        used.setRelationNumber(getUsingGiftCard().getCardNumber());

        CheckoutDataManager.getInstance().usePayment(used);
        onPaidSuccess();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
