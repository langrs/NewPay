package com.unioncloud.newpay.presentation.ui.home;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.raizlabs.coreutils.threading.ThreadingUtils;
import com.raizlabs.universaladapter.ViewHolder;
import com.raizlabs.universaladapter.converter.BaseAdapterConverter;
import com.raizlabs.universaladapter.converter.ItemClickedListener;
import com.raizlabs.universaladapter.converter.UniversalAdapter;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.presentation.model.DrawerItem;
import com.unioncloud.newpay.presentation.ui.LoadingFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 16/8/1.
 */
public class HomeFragment extends LoadingFragment {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private List<DrawerItem> drawerItemList = new ArrayList<>();
    private HomeViewHolder drawerViewBinder;
    private HomeDrawerAdapter adapter;
    private BaseAdapterConverter<DrawerItem, DrawerItemViewHolder> converter;
    private ItemClickedListener<DrawerItem, DrawerItemViewHolder> itemClickedListener
            = new ItemClickedListener<DrawerItem, DrawerItemViewHolder>() {
        @Override
        public void onItemClicked(UniversalAdapter<DrawerItem, DrawerItemViewHolder> adapter,
                                  DrawerItem drawerItem, DrawerItemViewHolder holder, int position) {
            // TODO
//            switch (DrawerItemMapper.DRAWER_ITEM_INDEX[drawerItem.destinationId.ordinal()])
        }
    };

    private int getItemPos(DrawerItem.ItemsIndex itemsIndex) {
        switch (itemsIndex) {

        }
        return -1;
    }

    private void showDrawerAction() {
        final View actionView = this.getActivity().getLayoutInflater().inflate(
                R.layout.view_navigation_drawer_action_menu, null);
        final PopupWindow popupWindow = new PopupWindow(actionView, -2, -2);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(this.getResources(), ""));
        actionView.findViewById(R.id.button_drawer_action).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO 执行耗时的操作
                        popupWindow.dismiss();
                    }
                });
        drawerViewBinder.contentListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    int width = drawerViewBinder.userActionButton.getWidth();
                    int measureSpec = View.MeasureSpec.makeMeasureSpec(5000, View.MeasureSpec.AT_MOST);
                    actionView.measure(measureSpec, measureSpec);
                    int offY = -drawerViewBinder.userActionButton.getPaddingBottom();
                    int measuredWidth = actionView.getMeasuredWidth();
                    int halfPaddingX = drawerViewBinder.userActionButton.getPaddingRight() / 2;
                    popupWindow.showAsDropDown(actionView, width - measuredWidth - halfPaddingX, offY);
                }
            }
        });
    }

//    private void f()
    private void f() {
//        com.raizlabs.n.a.a(new i(this));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer, container, false);
        drawerViewBinder = new HomeViewHolder(view);
        return view;
    }

    @Override
    public void onDestroyView() {
//        com.bhphoto.d.m.h().b(this);  // 移除耗时的响应回调
        this.converter.cleanup();
        this.converter = null;
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        // TODO remove the subscriber for cloud push.
        super.onPause();
    }

    @Override
    public void onResume() {
        // TODO subscribe the cloud push events
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int selectedPosition = getArguments().getInt("Selected Position", -1);
        adapter = new HomeDrawerAdapter(R.layout.view_drawer_item);
        adapter.currentItem = selectedPosition;
        ThreadingUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                drawerItemList.clear();

            }
        });
    }
}
