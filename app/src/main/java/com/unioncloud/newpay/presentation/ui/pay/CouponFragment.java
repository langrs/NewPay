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
import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;

/**
 * Created by cwj on 16/9/6.
 */
public class CouponFragment extends PayFragment {

//    预售券
    public static CouponFragment newPreSaleCoupon(){
        CouponFragment couponFragment = new CouponFragment();
        couponFragment.setCouponType(TYPE_COUPON_PRESALE);
        return couponFragment;
    }
    public static CouponFragment fillInPreSaleCoupon(){
        CouponFragment couponFragment = new CouponFragment();
        couponFragment.setCouponType(TYPE_COUPON_PRESALE);
        couponFragment.getArguments().putBoolean("isFillIn",true);
        return couponFragment;
    }
    /** 折扣券 */
    public static CouponFragment newDiscountCoupon(Coupon coupon) {
        CouponFragment fragment = new CouponFragment();
        fragment.setCouponType(TYPE_COUPON_DISCOUNT);
        fragment.setCoupon(coupon);
        return fragment;
    }

    public static CouponFragment fillInDiscountCoupon() {
        CouponFragment fragment = new CouponFragment();
        fragment.setCouponType(TYPE_COUPON_DISCOUNT);
        fragment.getArguments().putBoolean("isFillIn", true);
        return fragment;
    }

    /** 积分返利券 */
    public static CouponFragment fillInPointReturnCoupon() {
        CouponFragment fragment = new CouponFragment();
        fragment.setCouponType(TYPE_COUPON_POINT_RETURN);
        fragment.getArguments().putBoolean("isFillIn", true);
        return fragment;
    }

    public static CouponFragment newPointReturnCoupon(Coupon coupon) {
        CouponFragment fragment = new CouponFragment();
        fragment.setCouponType(TYPE_COUPON_POINT_RETURN);
        fragment.setCoupon(coupon);
        return fragment;
    }

    /** 购物券(服务器没有该券的数据) */
    public static CouponFragment newShoppingCoupon() {
        CouponFragment fragment = new CouponFragment();
        fragment.setCouponType(TYPE_COUPON_OUTSIDE);
        return fragment;

    }   /** 微信卡券(通过微信接口校验) */
    public static CouponFragment newWeChatCoupon(Coupon coupon) {
        CouponFragment fragment = new CouponFragment();
        fragment.setCoupon(coupon);
        fragment.setCouponType(TYPE_COUPON_WECHATA);
        return fragment;
    }
    public static CouponFragment fillInWeChatCoupon() {
        CouponFragment fragment = new CouponFragment();
        fragment.getArguments().putBoolean("isFillIn", true);
        fragment.setCouponType(TYPE_COUPON_WECHATA);
        return fragment;
    }

    private static final int TYPE_COUPON_OUTSIDE = 1;
    private static final int TYPE_COUPON_DISCOUNT = 2;
    private static final int TYPE_COUPON_POINT_RETURN = 3;
    private static final int TYPE_COUPON_WECHATA = 4;
    private static final int TYPE_COUPON_PRESALE = 5;

    private EditText couponNoEt;
    private View willpayContainer;

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
            case TYPE_COUPON_WECHATA:
                return PaymentSignpost.COUPON4;
            case TYPE_COUPON_PRESALE:
                return PaymentSignpost.PRESALE;
        }
        return null;
    }

    private void setCoupon(Coupon coupon) {
        getArguments().putSerializable("coupon", coupon);
    }

    private Coupon getUsingCoupon() {
        return (Coupon) getArguments().getSerializable("coupon");
    }
//是否外部券,购物券属于外部券,折扣券/积分返利券不属于外部券
//    判断的依据是打开这个Fragment是否有传入券对象信息,补录的也没有传入券对象信息的
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

        willpayContainer = view.findViewById(R.id.fragment_willpay_container);
        if (isOutsideCoupon()) { //外部券:购物券
            couponNoContainer.setVisibility(View.VISIBLE);
            couponValueContainer.setVisibility(View.GONE); //不需要显示券金额(券库获取,不能输入),只显示现支付(手工输入)
            amountInputTv.setText(""); //现支付金额(手工输入)
            ((EditText)amountInputTv).setSelection(amountInputTv.getText().length());
        } else { //内部券:折扣券/积分券,或者补录所有的券
            Coupon usingCoupon = getUsingCoupon();
            couponNoContainer.setVisibility(View.GONE); //券号隐藏
            couponValueContainer.setVisibility(View.VISIBLE);   //显示券金额
            String couponValueStr = usingCoupon.getCouponValue();
            couponValueTv.setText(MoneyUtils.fenToString(MoneyUtils.getFen(couponValueStr)));
            amountInputTv.setText(couponValueStr);  //默认现支付为券金额
            willpayContainer.setVisibility(View.GONE);  //隐藏现支付(手工输入框)
        }
    }

    @Override
    protected void pay(int unpay, int willPay) {
        boolean isOutside = isOutsideCoupon();
        String couponNo;
        int excessAmount = 0;
        if (isOutside || isFillIn()) {
            if (willPay > unpay) {
                showToast("支付金额已超出未付款项");
                return;
            }
            couponNo = couponNoEt.getText().toString();
//            非预售券,需要校验券号
            if(getCouponType() != TYPE_COUPON_PRESALE){
                if (TextUtils.isEmpty(couponNo)) {
                    showToast("无效的券号");
                    return;
                }
            }

        } else {
            Coupon usingCoupon = getUsingCoupon();
            couponNo = usingCoupon.getCouponNo();
            int couponValue = MoneyUtils.getFen(usingCoupon.getCouponValue());
            if (couponValue > unpay) {
                excessAmount = couponValue - willPay;
                willPay = unpay;
            } else {
                willPay = couponValue;
            }
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
