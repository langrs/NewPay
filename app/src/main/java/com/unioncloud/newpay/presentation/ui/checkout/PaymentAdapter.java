package com.unioncloud.newpay.presentation.ui.checkout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.unioncloud.newpay.R;
import com.unioncloud.newpay.presentation.ui.pay.PaymentSignpost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 16/8/15.
 */
public class PaymentAdapter extends BaseAdapter {

    private List<PaymentSignpost> list = new ArrayList<>();

    private Context context;

    public PaymentAdapter(Context context) {
        this.context = context;
    }

    public void setDataList(List<PaymentSignpost> list) {
        this.list.clear();
        if (list != null) {
            this.list.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 :list.size();
    }

    @Override
    public PaymentSignpost getItem(int position) {
        return list == null ? null : list.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.view_payment, parent, false);
            viewHolder.iconIv = (ImageView) convertView.findViewById(R.id.view_payment_icon);
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.view_payment_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PaymentSignpost paymentRes = list.get(position);
        viewHolder.iconIv.setImageResource(paymentRes.getIcon());
        viewHolder.nameTv.setText(paymentRes.getName());
        viewHolder.nameTv.setTextColor(paymentRes.getTextColor());
        return convertView;
    }

    private static class ViewHolder {
        public ImageView iconIv;
        public TextView nameTv;
    }
}
