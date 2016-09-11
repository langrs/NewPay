package com.unioncloud.newpay.presentation.ui.cart.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.presentation.ui.cart.CartItemMenuListener;

import java.util.List;

/**
 * Created by cwj on 16/8/13.
 */
public class CartItemAdapter extends BaseAdapter {

    List<CartItem> list;

    private Context context;
    CartItemMenuListener listener;

    public CartItemAdapter(Context context, CartItemMenuListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setData(List<CartItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 :list.size();
    }

    @Override
    public CartItem getItem(int position) {
        return list == null ? null :list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_cart, parent, false);
            viewHolder.view = (CartItemView)convertView.findViewById(R.id.list_item_cart_product);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.view.setCartItem(list.get(position));
        viewHolder.view.setCartItemMenuListener(listener);
        return convertView;
    }

    private static class ViewHolder {
        public CartItemView view;
    }
}
