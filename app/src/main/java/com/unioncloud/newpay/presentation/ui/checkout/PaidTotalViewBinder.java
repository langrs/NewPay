package com.unioncloud.newpay.presentation.ui.checkout;

import android.view.View;
import android.widget.TextView;

import com.raizlabs.coreutils.functions.Delegate;
import com.raizlabs.coreutils.threading.ThreadingUtils;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.presentation.model.payment.UsedPayments;

/**
 * Created by cwj on 16/8/15.
 */
public class PaidTotalViewBinder {

    public TextView paidTotalTv;
    public View paidContainerView;

    public PaidTotalViewBinder(View view) {
        paidTotalTv = (TextView) view.findViewById(R.id.fragment_checkout_paid_total);
        paidContainerView = view.findViewById(R.id.fragment_checkout_paid_total_container);
    }

    public void setUsedPayments(UsedPayments usedPayments) {
        final int paidTotal = usedPayments.getPaidTotal();
        ThreadingUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                paidTotalTv.setText(MoneyUtils.fenToString(paidTotal));
            }
        });
    }


}
