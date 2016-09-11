package com.unioncloud.newpay.presentation.ui.placeorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.product.Product;

import java.util.List;

/**
 * Created by cwj on 16/8/13.
 */
public class ProductAdapter extends BaseAdapter {

    private List<Product> list;

    public void setDataList(List<Product> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (list == null) ? 0 : list.size();
    }

    @Override
    public Product getItem(int position) {
        return (list == null) ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return bindView(position, convertView, parent, 0);
        return bindView(position, convertView, parent, 1);
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
                        .inflate(R.layout.spinner_item_product_number, parent, false);
//                        .inflate(R.layout.spinner_item_no_divider, parent, false);
            } else {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_product_number, parent, false);
//                        .inflate(R.layout.list_item_text_no_divider, parent, false);
            }
            if (type == 0) {
                holder.textView = (TextView) convertView.findViewById(R.id.spinner_item_product_number);
            } else {
                holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(list.get(position).getProductNumber());
        return convertView;
    }

    private static class ViewHolder {
        public TextView textView;
    }
}
