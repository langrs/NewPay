package com.unioncloud.newpay.presentation.ui.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.order.OrderStatisticsPayment;
import com.unioncloud.newpay.domain.utils.MoneyUtils;

import java.util.List;

/**
 * Created by cwj on 16/9/3.
 */
public class PayStatisticsAdapter extends BaseAdapter {

    private Context context;
    private List<OrderStatisticsPayment> list;

    public PayStatisticsAdapter(Context context, List<OrderStatisticsPayment> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.list_item_payment_count, parent, false);
            holder.nameTv = (TextView) convertView.findViewById(R.id.list_item_payment_name);
            holder.totalsTv = (TextView) convertView.findViewById(R.id.list_item_payment_totals);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderStatisticsPayment payStatistics = list.get(position);
        holder.nameTv.setText(payStatistics.paymentName);
        holder.countTv.setText(String.valueOf(payStatistics.payCount));
        holder.totalsTv.setText(MoneyUtils.fenToString(payStatistics.payTotals));
        return convertView;
    }

    static class ViewHolder {
        TextView nameTv;
        TextView countTv;
        TextView totalsTv;
    }
}