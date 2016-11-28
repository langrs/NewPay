package com.esummer.android.drawer;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

import com.esummer.android.FragmentManagerProvider;
import com.esummer.android.R;
import com.raizlabs.coreutils.functions.Delegate;

/**
 * Created by cwj on 16/7/11.
 */
public class DrawerHelper {
    private String drawerFragmentTag;
    private WrapperDrawerListener listenerSet = new WrapperDrawerListener();
    private FragmentManagerProvider fmProvider;
    private DrawerLayout drawerLayout;
    private View drawerView;
    private ActionBarDrawerToggle drawerToggle;

    public DrawerHelper(String drawerFragmentTag,
                        FragmentManagerProvider fmProvider) {
        this.drawerFragmentTag = drawerFragmentTag;
        this.fmProvider = fmProvider;
    }

    public void initDrawer(Activity activity, DrawerLayout drawerLayout,
                               int drawerViewId, boolean isDefaultListener, int selected) {
        this.drawerLayout = drawerLayout;
        this.drawerLayout.setScrimColor(Color.argb(102, 0, 0, 0));
        if (!hasDrawer()) {
            drawerView = drawerLayout.findViewById(drawerViewId);
            FragmentTransaction ft = getFM().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putInt("Selected Position", selected);
            ft.add(drawerViewId, createDrawerFragment(), drawerFragmentTag).commit();
            if(isDefaultListener) {
                this.drawerToggle = new ActionBarDrawerToggle(activity, drawerLayout,
                        R.string.drawer_action_show, R.string.drawer_action_hide);
                this.listenerSet.add(drawerToggle);
            }
            drawerLayout.setDrawerListener(listenerSet);
        }
    }

    protected static Fragment createDrawerFragment() {
        return null;
    }

    public void onConfigurationChanged(Configuration configuration) {
        if(hasDrawer()) {
            drawerToggle.onConfigurationChanged(configuration);
        }
    }

    public void addListener(DrawerLayout.DrawerListener listener) {
        listenerSet.add(listener);
    }

    public boolean hasSelected(MenuItem menuItem) {
        if (hasDrawer()) {
            return drawerToggle.onOptionsItemSelected(menuItem);
        }
        return false;
    }

    public boolean closeDrawer(final Delegate<DrawerHelper> listener) {
        if(hasDrawer()) {
            if(listener != null) {
                this.addListener(new DrawerLayout.SimpleDrawerListener() {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        listener.execute(DrawerHelper.this);
                        remove(this);
                    }
                });
            }
            drawerLayout.closeDrawer(drawerView);
            return true;
        } else {
            return false;
        }
    }

    public boolean hasDrawer() {
        return this.drawerView != null;
    }

    public boolean remove(DrawerLayout.DrawerListener listener) {
        return this.listenerSet.remove(listener);
    }

    public void clear() {
        if (this.drawerLayout != null) {
            this.drawerLayout.setDrawerListener(null);
        }
        this.drawerLayout = null;
        this.drawerToggle = null;
        this.drawerView = null;
        this.listenerSet.clear();
    }

    public void syncState() {
        if (hasDrawer()) {
            this.drawerToggle.syncState();
        }
    }

    public Fragment findDrawerFragment() {
        return getFM().findFragmentByTag(drawerFragmentTag);
    }

    protected FragmentManager getFM() {
        return getFmProvider().providerFM();
    }

    protected FragmentManagerProvider getFmProvider() {
        return fmProvider;
    }
}
