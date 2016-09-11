package com.unioncloud.newpay.presentation.ui.cart.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unioncloud.newpay.R;

/**
 * Created by cwj on 16/8/13.
 */
public class QuantityAdapter extends BaseAdapter {

    private static final int DEFAULT_MAX = 10;

    private int maxCount;

    public QuantityAdapter() {
        maxCount = DEFAULT_MAX;
    }

    public QuantityAdapter(int maxCount) {
        this.maxCount = maxCount;
    }

    @Override
    public int getCount() {
        return maxCount;
    }

    @Override
    public Object getItem(int position) {
        return position + 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return bindView(position, convertView, parent, 0);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return bindView(position, convertView, parent, 1);
    }

    private View bindView(int position, View convertView, ViewGroup parent, int type) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            if (type == 0) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.spinner_item_no_divider, parent, false);
            } else {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_text_no_divider, parent, false);
            }

            holder.textView = (TextView) convertView.findViewById(R.id.list_item_text_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (type == 0) {
            holder.textView.setText(String.format("数量%1$6d", position + 1));
        } else {
            holder.textView.setText(String.valueOf(position + 1));
        }
        return convertView;
    }

    private static class ViewHolder {
        public TextView textView;
    }
}
