package com.unioncloud.newpay.presentation.ui.notice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.notice.Notice;
import com.unioncloud.newpay.presentation.model.notice.NoticeModel;

import java.util.List;

/**
 * Created by cwj on 16/9/12.
 */
public class NoticesAdapter extends BaseAdapter {

    Context context;
    List<NoticeModel> list;

    public NoticesAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<NoticeModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public NoticeModel getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_notices, parent, false);
        }
        TextView noticeTv = (TextView) convertView.findViewById(R.id.list_item_notice);
        NoticeModel notice = list.get(position);
        noticeTv.setText(notice.getTitle());
        return convertView;
    }

}
