package com.unioncloud.newpay.presentation.ui.home;

import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.raizlabs.coreutils.view.ViewCompatibility;
import com.raizlabs.coreutils.view.ViewUtils;
import com.raizlabs.universaladapter.ListBasedAdapter;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.presentation.model.DrawerItem;

/**
 * Created by cwj on 16/8/1.
 */
public class HomeDrawerAdapter extends ListBasedAdapter<DrawerItem, DrawerItemViewHolder> {

    public int currentItem;
    private int itemViewLayout;

    public HomeDrawerAdapter(@LayoutRes int itemViewLayout) {
        this.itemViewLayout = itemViewLayout;
    }

    @Override
    protected DrawerItemViewHolder onCreateViewHolder(ViewGroup parent, int itemType) {
        return new DrawerItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(itemViewLayout, parent, false));
    }

    @Override
    protected void onBindViewHolder(DrawerItemViewHolder viewHolder,
                                    DrawerItem drawerItem, int position) {

        viewHolder.titleTextView.setText(drawerItem.titleResId);
        viewHolder.badgeTextView.setText(String.format("%d", drawerItem.badge));
        boolean hasBadge = (drawerItem.badge > 0);
        ViewUtils.setVisibleOrGone(viewHolder.badgeTextView, hasBadge);
        ViewUtils.setVisibleOrGone(viewHolder.topDivider, drawerItem.showTopDivider);
        ViewUtils.setVisibleOrGone(viewHolder.bottomDivider, drawerItem.showBottomDivider);

        if(viewHolder.titleTextView.getLineCount() > 1) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)viewHolder.titleTextView.getLayoutParams();
            layoutParams.gravity = Gravity.TOP;
            viewHolder.titleTextView.setLayoutParams(layoutParams);
            viewHolder.titleTextView.requestLayout();
        }

        if (currentItem == position) {
            ViewCompatibility.setViewBackground(viewHolder.itemView, viewHolder.itemView.getResources().getDrawable(R.drawable.bg_item_nav_drawer_selected));
            viewHolder.iconImageView.setImageResource(drawerItem.iconResSelectedId);
            viewHolder.titleTextView.setTextColor(viewHolder.itemView.getResources().getColor(drawerItem.colorResSelectedId));
        } else {
            ViewCompatibility.setViewBackground(viewHolder.itemView, viewHolder.itemView.getResources().getDrawable(R.drawable.bg_item_nav_drawer_pressed));
            viewHolder.iconImageView.setImageResource(drawerItem.iconResId);
            viewHolder.titleTextView.setTextColor(viewHolder.itemView.getResources().getColor(drawerItem.colorResId));
        }

    }
}
