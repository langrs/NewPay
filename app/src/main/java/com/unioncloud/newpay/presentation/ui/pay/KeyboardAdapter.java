package com.unioncloud.newpay.presentation.ui.pay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unioncloud.newpay.R;

import java.util.List;

/**
 * Created by cwj on 16/8/16.
 */
public class KeyboardAdapter extends BaseAdapter {
    private List<String> list;

    public KeyboardAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_keyboard, null);
        }
        TextView itemView = (TextView) convertView.findViewById(R.id.grid_item_keyboard_item);
        itemView.setText(getItem(position));
        return convertView;
    }
}
