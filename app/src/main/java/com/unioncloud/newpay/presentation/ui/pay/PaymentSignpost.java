package com.unioncloud.newpay.presentation.ui.pay;


import com.esummer.android.fragment.StatedFragment;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.presentation.ui.coupon.QueryCouponFragment;
import com.unioncloud.newpay.presentation.ui.gift.QueryGiftCardFragment;
import com.unioncloud.newpay.presentation.ui.refund.BankcardRefundFragment;
import com.unioncloud.newpay.presentation.ui.refund.CashRefundFragment;
import com.unioncloud.newpay.presentation.ui.refund.CouponRefundFragment;
import com.unioncloud.newpay.presentation.ui.refund.GiftCardRefundFragment;
import com.unioncloud.newpay.presentation.ui.refund.RefundFragment;
import com.unioncloud.newpay.presentation.ui.refund.ThirdPartyRefundFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 16/8/15.
 */
public enum PaymentSignpost {

    WECHAT(R.drawable.ic_checkout_wechat,
            R.color.TextColor_Payment_Wechat,
            "微信") {
        @Override
        public StatedFragment toPay() {
            return ThirdPartyPayFragment.newWechatPay();
        }

        @Override
        public int numberToInt() {
            return 4;
        }

        @Override
        public StatedFragment toRefund() {
            return ThirdPartyRefundFragment.newWechatPay();
        }
    },
    ALI(R.drawable.ic_checkout_alipay,
            R.color.TextColor_Payment_Ali,
            "支付宝") {
        @Override
        public StatedFragment toPay() {
            return ThirdPartyPayFragment.newAliPay();
        }

        @Override
        public int numberToInt() {
            return 3;
        }

        @Override
        public StatedFragment toRefund() {
            return ThirdPartyRefundFragment.newAliPay();
        }
    },
    BANKCARD(R.drawable.ic_checkout_bankcard,
            R.color.TextColor_Payment_Bankcard,
            "银行卡") {
        @Override
        public StatedFragment toPay() {
            return BankcardFragment.newInstance();
        }

        @Override
        public int numberToInt() {
            return 2;
        }

        @Override
        public RefundFragment toRefund() {
            return BankcardRefundFragment.newInstance();
        }
    },
    CASH(R.drawable.ic_checkout_cash,
            R.color.TextColor_Payment_Cash,
            "现金") {
        @Override
        public StatedFragment toPay() {
            return CashFragment.newInstance();
        }

        @Override
        public int numberToInt() {
            return 1;
        }

        @Override
        public StatedFragment toRefund() {
            return CashRefundFragment.newInstance();
        }
    },
    GIFT(R.drawable.ic_checkout_gift,
            R.color.TextColor_Payment_Gift,
            "储值卡") {
        @Override
        public StatedFragment toPay() {
            return QueryGiftCardFragment.newPay();
        }

        @Override
        public int numberToInt() {
            return 5;
        }

        @Override
        public StatedFragment toRefund() {
            return QueryGiftCardFragment.newRefund();
        }
    },
    COUPON(R.drawable.ic_checkout_coupon,
            R.color.TextColor_Payment_Coupon,
            "购物券") {
        @Override
        public StatedFragment toPay() {
            return CouponFragment.newCoupon();
        }

        @Override
        public int numberToInt() {
            return 6;
        }

        @Override
        public StatedFragment toRefund() {
            return CouponRefundFragment.newCoupon();
        }
    },
    COUPON2(R.drawable.ic_checkout_coupon,
           R.color.TextColor_Payment_Coupon,
           "折扣券") {
        @Override
        public StatedFragment toPay() {
            return QueryCouponFragment.newInstance();
        }

        @Override
        public int numberToInt() {
            return 7;
        }

        @Override
        public StatedFragment toRefund() {
            return CouponRefundFragment.newDiscountCoupon();
        }
    },
    COUPON3(R.drawable.ic_checkout_coupon,
            R.color.TextColor_Payment_Coupon,
            "积分返利券") {
        @Override
        public StatedFragment toPay() {
            return QueryCouponFragment.newInstance();
        }

        @Override
        public int numberToInt() {
            return 8;
        }

        @Override
        public StatedFragment toRefund() {
            return CouponRefundFragment.newPointReturnCoupon();
        }
    };

    private int icon;
    private int textColor;
    private String name;

    PaymentSignpost(int icon, int textColor, String name) {
        this.icon = icon;
        this.textColor = textColor;
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public int getTextColor() {
        return textColor;
    }

    public String getName() {
        return name;
    }

    public abstract int numberToInt();

    public abstract StatedFragment toPay();

    public abstract StatedFragment toRefund();

    public boolean supportPay() {
        return true;
    }

    public boolean supportRefund() {
        return true;
    }

    public static List<PaymentSignpost> filter(List<Payment> paymentList) {
        ArrayList<PaymentSignpost> list = new ArrayList<>();
        if (paymentList == null) {
            return list;
        }
        boolean hasInsideCoupon = false;
        for (Payment payment : paymentList) {
            PaymentSignpost signpost = getSignpost(payment.getPaymentNumber());
            if (signpost != null) {
                // 蛋疼,折扣券和积分返利券展示的时候只显示折扣券
                if (signpost == COUPON2 || signpost == COUPON3) {
                    if (!hasInsideCoupon) {
                        hasInsideCoupon = true;
                        list.add(COUPON2);
                    }
                } else {
                    list.add(signpost);
                }
            }
        }
        return list;
    }

    public static boolean needPrintBillNo(Payment payment) {
        if (payment == null || payment.getPaymentNumber() == null) {
            return false;
        }
        int number = Integer.valueOf(payment.getPaymentNumber());
        return (number == 5) ||
               (number == 6) ||
               (number == 7) ||
               (number == 8);
    }

    public static PaymentSignpost getSignpost(String paymentNumber) {
        if (paymentNumber == null) {
            return null;
        }
        int number = Integer.valueOf(paymentNumber);
        switch (number) {
            case 1:
                return CASH;
            case 2:
                return BANKCARD;
            case 3:
                return ALI;
            case 4:
                return WECHAT;
            case 5:
                return GIFT;
            case 6:
                return COUPON;
            case 7:
                return COUPON2;
            case 8:
                return COUPON3;
            default:
                return null;
        }
    }
}
