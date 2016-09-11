package com.unioncloud.newpay.presentation.ui.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.raizlabs.universaladapter.ViewHolder;
import com.unioncloud.newpay.R;

/**
 * Created by cwj on 16/8/1.
 */
public class DrawerItemViewHolder extends ViewHolder {

    protected ImageView iconImageView;
    protected TextView titleTextView;
    protected TextView badgeTextView;
    protected View topDivider;
    protected View bottomDivider;

    public DrawerItemViewHolder(View itemView) {
        super(itemView);
        iconImageView = (ImageView)itemView.findViewById(R.id.view_drawer_item_icon);
        titleTextView = (TextView)itemView.findViewById(R.id.view_drawer_item_title);
        badgeTextView = (TextView)itemView.findViewById(R.id.view_drawer_item_badge);
        topDivider = itemView.findViewById(R.id.view_drawer_item_top_divider);
        bottomDivider = itemView.findViewById(R.id.view_drawer_item_bottom_divider);

    }
}
