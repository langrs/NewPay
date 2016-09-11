package com.unioncloud.newpay.presentation.ui.home;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.raizlabs.universaladapter.ViewHolder;
import com.unioncloud.newpay.R;

/**
 * Created by cwj on 16/8/1.
 */
public class HomeViewHolder extends ViewHolder {
    public ViewGroup userLayout;
    public ViewGroup contentListView;
    public TextView greetingTextView;
    public ImageView userActionButton;

    public HomeViewHolder(View view) {
        super(view);
        if(view != null) {
            userLayout = (ViewGroup)view.findViewById(R.id.fragment_drawer_user_layout);
            greetingTextView = (TextView)view.findViewById(R.id.fragment_drawer_greeting);
            userActionButton = (ImageView)view.findViewById(R.id.fragment_drawer_account_action_button);
            contentListView = (ViewGroup)view.findViewById(R.id.fragment_drawer_contentList);
        }
    }
}
