package com.unioncloud.newpay.presentation.ui.refund;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.model.refund.RefundDataManager;
import com.unioncloud.newpay.presentation.ui.pay.PaymentSignpost;

/**
 * Created by cwj on 16/9/8.
 */
public class CouponRefundFragment extends RefundFragment {

    public static CouponRefundFragment newCoupon() {
        CouponRefundFragment fragment = new CouponRefundFragment();
        fragment.getArguments().putInt("couponType", TYPE_COUPON);
        return fragment;
    }

    public static CouponRefundFragment newDiscountCoupon() {
        CouponRefundFragment fragment = new CouponRefundFragment();
        fragment.getArguments().putInt("couponType", TYPE_COUPON_DISCOUNT);
        return fragment;
    }

    public static CouponRefundFragment newPointReturnCoupon() {
        CouponRefundFragment fragment = new CouponRefundFragment();
        fragment.getArguments().putInt("couponType", TYPE_COUPON_POINT_RETURN);
        return fragment;
    }

    private static final int TYPE_COUPON = 1;
    private static final int TYPE_COUPON_DISCOUNT = 2;
    private static final int TYPE_COUPON_POINT_RETURN = 3;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        originalIdLabel.setText("原券号:");
    }

    @Override
    protected PaymentSignpost getPaymentSignpost() {
        int type = getArguments().getInt("couponType");
        switch (type) {
            case TYPE_COUPON:
                return PaymentSignpost.COUPON;
            case TYPE_COUPON_DISCOUNT:
                return PaymentSignpost.COUPON2;
            case TYPE_COUPON_POINT_RETURN:
                return PaymentSignpost.COUPON3;
        }
        return null;
    }

    @Override
    protected void refund() {
        Payment cash = PosDataManager.getInstance().getPaymentByNumberInt(
                getPaymentSignpost().numberToInt());

        PaymentUsed used = new PaymentUsed();
        used.setPaymentId(cash.getPaymentId());
        used.setPaymentName(cash.getPaymentName());
        used.setPaymentQy(cash.getPaymentQy());
        used.setPayAmount(-getRefundAmount());
        used.setExcessAmount(0);
        used.setCurrencyId(cash.getCurrencyId());
        used.setExchangeRate(cash.getExchangeRate());
        used.setRelationNumber(getOriginalBillNo());
        RefundDataManager.getInstance().usePayment(used);
        onRefundSuccess();
    }
}
