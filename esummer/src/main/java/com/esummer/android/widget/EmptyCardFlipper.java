package com.esummer.android.widget;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.esummer.android.R;
import com.raizlabs.coreutils.view.ViewUtils;

/**
 * Created by cwj on 16/8/4.
 */
public class EmptyCardFlipper extends ViewFlipper {

    public Button addItemButton;
    public FrameLayout contentContainer;
    protected CardView detailContainer;
    public TextView contentTitle;
    int addItemButtonIndex;
    int detailContainerIndex;

    public EmptyCardFlipper(Context context) {
        super(context);
    }

    public EmptyCardFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public View setContentView(View view) {
        if (contentContainer != null) {
            contentContainer.removeAllViews();
            contentContainer.addView(view);
            return view;
        }
        return null;
    }

    protected void inflateView(Context context) {
        View.inflate(context, R.layout.view_empty_card_flipper, this);
    }

    protected void obtainAttrs(AttributeSet attrs) {
        inflateView(getContext().getApplicationContext());
        addItemButton = (Button) findViewById(R.id.view_empty_card_flipper_addItemButton);
        contentContainer = (FrameLayout) findViewById(R.id.view_empty_card_flipper_contentContainer);
        detailContainer = (CardView) findViewById(R.id.view_empty_card_flipper_detail_container);
        addItemButtonIndex = indexOfChild(addItemButton);
        detailContainerIndex = indexOfChild(detailContainer);
        setMeasureAllChildren(false);   // 让ViewFlipper滑动的时候高度适应当前视图高度,而不是子视图中最高的为准

        contentTitle = (TextView) findViewById(R.id.view_empty_card_flipper_title);
    }

    public void setAddItemButtonDrawables(@DrawableRes int left, @DrawableRes int top,
                                          @DrawableRes int right, @DrawableRes int bottom) {
        if (addItemButton != null) {
            addItemButton.setVisibility(VISIBLE);
            addItemButton.setCompoundDrawablesRelativeWithIntrinsicBounds(left, top, right, bottom);
        }
    }

    public void setCardTitleDrawables(@DrawableRes int left, @DrawableRes int top,
                                         @DrawableRes int right, @DrawableRes int bottom) {
        if (contentTitle != null) {
            contentTitle.setVisibility(VISIBLE);
            contentTitle.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        }
    }

    public void setCardTitlePadding(int left, int top, int right, int bottom) {
        if (contentTitle != null) {
            contentTitle.setPadding(left, top, right, bottom);
        }
    }

    public void cleanCardTitleDrawables() {
        if (contentTitle != null) {
            contentTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    public void setCardTitle(@StringRes int title) {
        contentTitle.setText(title);
        ViewUtils.setVisibleOrGone(contentTitle, (title != 0));
    }

    public void setCardTitle(String title) {
        if (contentTitle != null) {
            contentTitle.setVisibility(VISIBLE);
            contentTitle.setText(title);
        }
    }

    public void setCardTitleColorResource(@ColorRes int titleColorRes) {
        if ((titleColorRes != 0) && (contentTitle != null)) {
            contentTitle.setTextColor(getResources().getColor(titleColorRes));
        }
    }

    public void showContentView() {
        setDisplayedChild(detailContainerIndex);
    }

    public View getContentView() {
        if ((contentContainer != null) && (contentContainer.getChildCount() > 0)) {
            return contentContainer.getChildAt(0);
        }
        return null;
    }

    public void showEmptyView() {
        setDisplayedChild(addItemButtonIndex);
    }

    public boolean addable() {
        return addItemButton.isEnabled();
    }

    public void setAddItemListener(OnClickListener listener) {
        addItemButton.setOnClickListener(listener);
    }

    public void setContentViewClickListener(OnClickListener listener) {
        if (detailContainer != null) {
            detailContainer.setOnClickListener(listener);
        }
    }

    public void setEmptyStateActionEnabled(boolean isEnable) {
        if (addItemButton != null) {
            addItemButton.setEnabled(isEnable);
        }
    }

    public void setEmptyStateButtonColorResource(int colorId) {
        if(colorId != 0) {
            addItemButton.setTextColor(getResources().getColor(colorId));
        }
    }

    public void setEmptyStateButtonTitle(int title) {
        if(addItemButton != null) {
            addItemButton.setText(title);
        }
    }

    public void setEmptyStateButtonTitle(String title) {
        if(addItemButton != null) {
            addItemButton.setText(title);
        }
    }
}
