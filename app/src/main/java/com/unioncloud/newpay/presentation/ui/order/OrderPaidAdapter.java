package com.unioncloud.newpay.presentation.ui.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.utils.MoneyUtils;

import java.util.List;

/**
 * Created by cwj on 16/8/26.
 */
public class OrderPaidAdapter extends BaseAdapter {

    List<PaymentUsed> list;
    Context context;

    public OrderPaidAdapter(Context context, List<PaymentUsed> list) {
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
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.list_item_paid_info, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.list_item_paid_info_payment_name);
            holder.amount = (TextView) convertView.findViewById(R.id.list_item_paid_info_payment_amount);
            holder.billNo = (TextView) convertView.findViewById(R.id.list_item_paid_info_billNo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PaymentUsed used = list.get(position);

        holder.name.setText(MoneyUtils.fenToString(used.getPayAmount()));
        holder.amount.setText(MoneyUtils.fenToString(used.getPayAmount()));
        holder.billNo.setText(used.getRelationNumber());
        return convertView;
    }

    private static class ViewHolder {
        public TextView name;
        public TextView amount;
        public TextView billNo;
    }
}
