package com.unioncloud.newpay.presentation.ui.pay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.erp.GiftCoupon;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;

/**
 * Created by cwj on 16/9/6.
 */
public class CouponFragment extends PayFragment {

    /** 折扣券 */
    public static CouponFragment newDiscountCoupon(GiftCoupon coupon) {
        CouponFragment fragment = new CouponFragment();
        fragment.setCouponType(TYPE_COUPON_DISCOUNT);
        fragment.setCoupon(coupon);
        return fragment;
    }

    /** 积分返利券 */
    public static CouponFragment newPointReturnCoupon(GiftCoupon coupon) {
        CouponFragment fragment = new CouponFragment();
        fragment.setCouponType(TYPE_COUPON_POINT_RETURN);
        fragment.setCoupon(coupon);
        return fragment;
    }

    /** 购物券(服务器没有该券的数据) */
    public static CouponFragment newCoupon() {
        CouponFragment fragment = new CouponFragment();
        fragment.setCouponType(TYPE_COUPON_OUTSIDE);
        return fragment;
    }

    private static final int TYPE_COUPON_OUTSIDE = 1;
    private static final int TYPE_COUPON_DISCOUNT = 2;
    private static final int TYPE_COUPON_POINT_RETURN = 3;

    private EditText couponNoEt;

    private void setCouponType(int couponType) {
        getArguments().putInt("couponType", couponType);
    }

    private int getCouponType() {
        return getArguments().getInt("couponType");
    }

    @Override
    protected PaymentSignpost getPaymentSignpost() {
        int couponType = getCouponType();
        switch (couponType) {
            case TYPE_COUPON_OUTSIDE:
                return PaymentSignpost.COUPON;
            case TYPE_COUPON_DISCOUNT:
                return PaymentSignpost.COUPON2;
            case TYPE_COUPON_POINT_RETURN:
                return PaymentSignpost.COUPON3;
        }
        return null;
    }

    private void setCoupon(GiftCoupon coupon) {
        getArguments().putSerializable("coupon", coupon);
    }

    private GiftCoupon getUsingCoupon() {
        return (GiftCoupon) getArguments().getSerializable("coupon");
    }

    private boolean isOutsideCoupon() {
        return !getArguments().containsKey("coupon");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pay_coupon, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View couponNoContainer = view.findViewById(R.id.fragment_pay_couponNo_container);
        couponNoEt = (EditText) view.findViewById(R.id.fragment_pay_couponNo);
        View couponValueContainer = view.findViewById(R.id.fragment_pay_coupon_value_container);
        TextView couponValueTv = (TextView) view.findViewById(R.id.fragment_pay_coupon_value);
        if (isOutsideCoupon()) {
            couponNoContainer.setVisibility(View.VISIBLE);
            couponValueContainer.setVisibility(View.GONE);
        } else {
            GiftCoupon usingCoupon = getUsingCoupon();
            couponNoContainer.setVisibility(View.GONE);
            couponValueContainer.setVisibility(View.VISIBLE);
            couponValueTv.setText(MoneyUtils.fenToString(MoneyUtils.getFen(usingCoupon.getPrice())));
        }
        ((EditText)amountInputTv).setSelection(amountInputTv.getText().length());
    }

    @Override
    protected void pay(int unpay, int willPay) {
        if (willPay > unpay) {
            showToast("支付金额已超出未付款项");
            return;
        }
        boolean isOutside = isOutsideCoupon();
        String couponNo;
        int excessAmount = 0;
        if (isOutside) {
            couponNo = couponNoEt.getText().toString();
            if (TextUtils.isEmpty(couponNo)) {
                showToast("无效的券号");
                return;
            }
        } else {
            GiftCoupon usingCoupon = getUsingCoupon();
            int couponValue = MoneyUtils.getFen(usingCoupon.getPrice());
            if (willPay > couponValue) {
                showToast("券金额不足,请重新输入");
                return;
            }
            couponNo = usingCoupon.getCouponNumber();
            excessAmount = couponValue - willPay;
        }

        PaymentSignpost signpost = getPaymentSignpost();
        Payment payment = PosDataManager.getInstance().getPaymentByNumberInt(signpost.numberToInt());
        PaymentUsed used = new PaymentUsed();
        used.setPaymentId(payment.getPaymentId());
        used.setPaymentName(payment.getPaymentName());
        used.setPaymentQy(payment.getPaymentQy());
        used.setPayAmount(willPay);
        used.setExcessAmount(excessAmount);
        used.setCurrencyId(payment.getCurrencyId());
        used.setExchangeRate(payment.getExchangeRate());
        used.setRelationNumber(couponNo);

        CheckoutDataManager.getInstance().usePayment(used);
        onPaidSuccess();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
