package com.esummer.android.drawer;

import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.raizlabs.coreutils.collections.MappableSet;
import com.raizlabs.coreutils.functions.Delegate;

/**
 * Created by cwj on 16/7/11.
 */
class WrapperDrawerListener extends MappableSet<DrawerLayout.DrawerListener> implements DrawerLayout.DrawerListener {

    @Override
    public void onDrawerSlide(final View drawerView, float slideOffset) {
        map(new Delegate<DrawerLayout.DrawerListener>() {
            @Override
            public void execute(DrawerLayout.DrawerListener drawerListener) {
                drawerListener.onDrawerClosed(drawerView);
            }
        });
    }

    @Override
    public void onDrawerOpened(final View drawerView) {
        map(new Delegate<DrawerLayout.DrawerListener>() {
            @Override
            public void execute(DrawerLayout.DrawerListener drawerListener) {
                drawerListener.onDrawerOpened(drawerView);
            }
        });
    }

    @Override
    public void onDrawerClosed(final View drawerView) {
        map(new Delegate<DrawerLayout.DrawerListener>() {
            @Override
            public void execute(DrawerLayout.DrawerListener drawerListener) {
                drawerListener.onDrawerClosed(drawerView);
            }
        });
    }

    @Override
    public void onDrawerStateChanged(final int newState) {
        map(new Delegate<DrawerLayout.DrawerListener>() {
            @Override
            public void execute(DrawerLayout.DrawerListener drawerListener) {
                drawerListener.onDrawerStateChanged(newState);
            }
        });
    }
}
