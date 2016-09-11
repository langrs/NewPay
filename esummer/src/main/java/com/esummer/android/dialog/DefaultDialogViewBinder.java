package com.esummer.android.dialog;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.esummer.android.R;

/**
 * Created by cwj on 16/7/7.
 */
class DefaultDialogViewBinder {
    public ScrollView contentScrollView;
    public TextView titleView;
    public TextView messageView;
    public ProgressBar progressBar;
    public LinearLayout textHolder;
    public TextView negative;
    public TextView neutral;
    public TextView positive;
    public LinearLayout footer;

    public void bind(View view) {
        this.contentScrollView = (ScrollView)view.findViewById(R.id.view_dialog_scrollView);
        this.titleView = (TextView)view.findViewById(R.id.dialog_title);
        this.messageView = (TextView)view.findViewById(R.id.dialog_message);
        this.progressBar = (ProgressBar)view.findViewById(R.id.dialog_indeterminate);
        this.textHolder = (LinearLayout)view.findViewById(R.id.dialog_text_holder);
        this.negative = (TextView)view.findViewById(R.id.dialog_negative);
        this.neutral = (TextView)view.findViewById(R.id.dialog_neutral);
        this.positive = (TextView)view.findViewById(R.id.dialog_positive);
        this.footer = (LinearLayout)view.findViewById(R.id.dialog_footer);
    }
}
