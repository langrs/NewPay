package com.unioncloud.newpay.presentation.ui.order;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.unioncloud.newpay.domain.model.order.SaleOrder;

import java.util.List;

/**
 * Created by cwj on 16/9/2.
 */
public class OrderAdapter extends BaseAdapter {

    Context context;
    List<SaleOrder> list;

    public OrderAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<SaleOrder> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public SaleOrder getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new OrderItemView(context);
        }

        SaleOrder order = list.get(position);
        OrderItemView itemView = (OrderItemView) convertView;
        itemView.setOrderId(order.getHeader().getSaleNumber());
        itemView.setOrderDateTime(order.getHeader().getSaleDate());
        itemView.setOrderTotal(order.getHeader().getSaleAmount());
        return itemView;
    }
}
