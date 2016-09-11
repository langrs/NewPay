package com.unioncloud.newpay.presentation.ui.order;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.utils.MoneyUtils;

/**
 * 订单历史列表item
 */
public class OrderItemView extends RelativeLayout {

    private TextView orderIdTv;
    private TextView orderTotalTv;
    private TextView orderDateTimeTv;

    public OrderItemView(Context context) {
        super(context);
        init(context);
    }

    public OrderItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OrderItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_order_item, this);
        orderIdTv = (TextView) view.findViewById(R.id.view_order_item_orderId);
        orderTotalTv = (TextView) view.findViewById(R.id.view_order_item_total);
        orderDateTimeTv = (TextView) view.findViewById(R.id.view_order_item_order_date);
    }

    public void setOrderId(String orderId) {
        orderIdTv.setText(orderId);
    }

    public void setOrderTotal(String orderTotal) {
        orderTotal = MoneyUtils.fenToString(MoneyUtils.getFen(orderTotal));
        orderTotalTv.setText(String.format("¥ %1s", orderTotal));
    }

    public void setOrderDateTime(String dateTime) {
        orderDateTimeTv.setText(dateTime);
    }

}
