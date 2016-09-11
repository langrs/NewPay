package com.unioncloud.newpay.presentation.ui.home;

/**
 * Created by cwj on 16/7/27.
 */
public enum HomeDrawerItem {
    HOME("HOME", 0),
    SEARCH("查询", 1),
    ORDER_HISTORY("历史订单", 2),
    PREFERENCES("偏好设置", 3),
    ABOUT_US("关于", 4);

    String title;
    int index;

    HomeDrawerItem(String title, int index) {
        this.title = title;
        this.index = index;
    }
}
