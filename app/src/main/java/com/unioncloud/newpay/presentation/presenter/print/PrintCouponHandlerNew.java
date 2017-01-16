package com.unioncloud.newpay.presentation.presenter.print;

import android.app.Activity;

import com.unioncloud.newpay.domain.model.erp.Coupon;

/**
 * 不再通过Activity打印图片
 */

public class PrintCouponHandlerNew extends PrintCouponHandler {

    public PrintCouponHandlerNew(Activity context, Coupon coupon) {
        super(context, coupon);
    }


}
