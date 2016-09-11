package com.unioncloud.newpay.presentation.ui.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.order.OrderStatistics;
import com.unioncloud.newpay.domain.model.order.OrderStatisticsPayment;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;

import java.util.List;

/**
 * Created by cwj on 16/9/3.
 */
public class OrderStatisticsBinder {
    public TextView saleCountTv;
    public TextView saleTotalTv;
    public TextView refundCountTv;
    public TextView refundTotalTv;
    public TextView statisticsTotalTv;
    public LinearLayout paymentsView;
    private Context context;

    public OrderStatisticsBinder(View view) {
        context = view.getContext();
        saleCountTv = (TextView) view.findViewById(R.id.fragment_order_today_sale_count);
        saleTotalTv = (TextView) view.findViewById(R.id.fragment_order_today_sale_total);
        refundCountTv = (TextView) view.findViewById(R.id.fragment_order_today_refund_count);
        refundTotalTv = (TextView) view.findViewById(R.id.fragment_order_today_refund_totals);
        statisticsTotalTv = (TextView) view.findViewById(R.id.fragment_order_today_totals);
        paymentsView = (LinearLayout) view.findViewById(R.id.layout_order_statistics_payments);
    }

    public void setOrderStatistics(OrderStatistics statistics) {
        saleCountTv.setText(statistics.getSaleCount() + "单");
        saleTotalTv.setText(MoneyUtils.fenToString(statistics.getSaleTotals()));
        refundCountTv.setText(statistics.getRefundCount() + "单");
        refundTotalTv.setText(MoneyUtils.fenToString(statistics.getRefundTotal()));
        statisticsTotalTv.setText(MoneyUtils.fenToString(statistics.getStatisticsTotal()));
        List<OrderStatisticsPayment> list = statistics.getPayStatistics();
        PosDataManager posDataManager = PosDataManager.getInstance();
        for (OrderStatisticsPayment osp : list) {
            Payment payment = posDataManager.getPaymentById(osp.paymentId);
            osp.paymentName = payment.getPaymentName();
            View childView = LayoutInflater.from(context).inflate(R.layout.list_item_payment_count, paymentsView, false);
            TextView nameTv  = (TextView) childView.findViewById(R.id.list_item_payment_name);
            TextView totalsTv = (TextView) childView.findViewById(R.id.list_item_payment_totals);
            nameTv.setText(osp.paymentName);
            totalsTv.setText(MoneyUtils.fenToString(osp.payTotals));
            paymentsView.addView(childView);
        }
    }

    public void resetView() {
        saleCountTv.setText("0单");
        saleTotalTv.setText("0.00");
        refundCountTv.setText("0单");
        refundTotalTv.setText("0.00");
        statisticsTotalTv.setText("0.00");
        paymentsView.removeAllViews();
    }
}
