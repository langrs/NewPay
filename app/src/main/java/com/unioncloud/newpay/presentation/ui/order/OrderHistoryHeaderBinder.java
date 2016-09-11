package com.unioncloud.newpay.presentation.ui.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.utils.MoneyUtils;

/**
 * Created by cwj on 16/9/4.
 */
public class OrderHistoryHeaderBinder {
    public TextView totalTv;
    public TextView countTv;
    private Context context;
    public View createView(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.header_order_item, null);
        totalTv = (TextView) view.findViewById(R.id.header_order_item_total);
        countTv = (TextView) view.findViewById(R.id.header_order_item_count);
        return view;
    }

    public void setSum(int count, int total) {
        countTv.setText(context.getString(R.string.OrderHistory_Order_Count, count));
        totalTv.setText(context.getString(R.string.OrderHistory_Order_Total,
                MoneyUtils.fenToString(total)));
    }
}
