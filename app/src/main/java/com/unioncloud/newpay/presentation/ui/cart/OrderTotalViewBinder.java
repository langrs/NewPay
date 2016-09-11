package com.unioncloud.newpay.presentation.ui.cart;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.raizlabs.coreutils.functions.Delegate;
import com.raizlabs.coreutils.threading.ThreadingUtils;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.checkout.OrderTotals;

/**
 * Created by cwj on 16/8/10.
 */
public class OrderTotalViewBinder implements Delegate<OrderTotals>{

    private TextView subtotalTv;        // 总计
    private TextView totalDiscountTv;   // 总折扣
    private TextView totalTv;           // 应付总额

    public OrderTotalViewBinder(View view) {
        subtotalTv = (TextView) view.findViewById(R.id.view_order_total_subtotal);
        totalDiscountTv = (TextView) view.findViewById(R.id.view_order_total_discount);
        totalTv = (TextView) view.findViewById(R.id.view_order_total_orderTotal);
    }

    private void setAmountNumber(TextView textView, String amount) {
        if (TextUtils.isEmpty(amount) || Float.valueOf(amount) == 0F) {
            textView.setText("0.00");
        } else {
            textView.setText(amount);
        }
    }

    public void setOrderTotal(final OrderTotals orderTotals) {
        ThreadingUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                CheckoutDataManager.getInstance().getSelectedOrderType();
                subtotalTv.setText(MoneyUtils.fenToString(orderTotals.subtotal));
                totalDiscountTv.setText(MoneyUtils.fenToString(orderTotals.discount));
                totalTv.setText(MoneyUtils.fenToString(orderTotals.total));
            }
        });
    }

    @Override
    public void execute(OrderTotals orderTotals) {
        setOrderTotal(orderTotals);
    }
}
