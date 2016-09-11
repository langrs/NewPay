package com.unioncloud.newpay.presentation.ui.home;

import com.esummer.android.drawer.DrawerHelper;

/**
 * Created by cwj on 16/7/27.
 */
public interface HomeDrawerStateListener {

    void onCartUpdate(int cartItemCount);

    DrawerHelper getDrawerHelper();
}
