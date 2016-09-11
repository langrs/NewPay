package com.unioncloud.newpay.presentation.ui.home;

import com.unioncloud.newpay.presentation.model.DrawerItem.ItemsIndex;
import com.unioncloud.newpay.presentation.model.DrawerItem;

/**
 * Created by cwj on 16/8/1.
 */
public class DrawerItemMapper {

    public static final int DRAWER_ITEM_INDEX[];

    static {
        ItemsIndex[] itemsIndices = ItemsIndex.values();
        DRAWER_ITEM_INDEX = new int[itemsIndices.length];
        for (ItemsIndex itemsIndex : itemsIndices) {
            DRAWER_ITEM_INDEX[itemsIndex.ordinal()] = itemsIndex.ordinal();
        }
    }
}
