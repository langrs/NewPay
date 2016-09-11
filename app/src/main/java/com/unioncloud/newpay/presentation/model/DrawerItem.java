package com.unioncloud.newpay.presentation.model;

import com.unioncloud.newpay.presentation.ui.home.HomeDrawerItem;

/**
 * Created by cwj on 16/7/27.
 */
public class DrawerItem {
    public int badge;
    public int colorResId;
    public int colorResSelectedId;
    public ItemsIndex destinationId;
    public int iconResId;
    public int iconResSelectedId;
    public boolean showBottomDivider;
    public boolean showTopDivider;
    public int titleResId;

    public DrawerItem(int titleResId, int iconResId, int colorResId, int iconResSelectedId, int colorResSelectedId,
                      boolean showTopDivider, boolean showBottomDivider, ItemsIndex destinationId) {
        this.titleResId = titleResId;
        this.iconResId = iconResId;
        this.colorResId = colorResId;
        this.iconResSelectedId = iconResSelectedId;
        this.colorResSelectedId = colorResSelectedId;
        this.showTopDivider = showTopDivider;
        this.showBottomDivider = showBottomDivider;
        this.destinationId = destinationId;
    }

    public enum ItemsIndex {
        HOME,
        SEARCH,
        ORDER_HISTORY,
        PREFERENCES,
        ABOUT_US;
    }
}
