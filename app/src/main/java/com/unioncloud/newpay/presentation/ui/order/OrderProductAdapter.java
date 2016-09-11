package com.unioncloud.newpay.presentation.ui.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.utils.MoneyUtils;

import java.util.List;

/**
 * Created by cwj on 16/8/26.
 */
public class OrderProductAdapter extends BaseAdapter {

    List<CartItem> list;
    private Context context;

    public OrderProductAdapter(Context context, List<CartItem> list) {
        this.list = list;
        this.context = context;
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
                    inflate(R.layout.list_item_order_product, parent, false);
            holder.productName = (TextView) convertView.findViewById(R.id.list_item_order_product_name);
            holder.productNumber = (TextView) convertView.findViewById(R.id.list_item_order_product_number);
            holder.productPrice = (TextView) convertView.findViewById(R.id.list_item_order_product_price);
            holder.productQuantity = (TextView) convertView.findViewById(R.id.list_item_order_product_quantity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CartItem cartItem = list.get(position);
        holder.productName.setText(cartItem.getProductName());
        holder.productNumber.setText(cartItem.getProductNumber());
        holder.productPrice.setText(String.format("¥ %1s", MoneyUtils.fenToString(cartItem.getSellUnitPrice())));
        holder.productQuantity.setText(String.format("数量%1$6d", cartItem.getQuantity()));

        return convertView;
    }

    private static class ViewHolder {
        public TextView productName;
        public TextView productNumber;
        public TextView productPrice;
        public TextView productQuantity;
    }
}
