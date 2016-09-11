package com.esummer.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.esummer.android.R;
import com.raizlabs.coreutils.threading.ThreadingUtils;

/**
 * Created by cwj on 16/3/18.
 */
public class LoadingView extends ViewFlipper {

    private View loadingView;
    private View contentView;

    private int indexOfLoading;
    private int indexOfContent;

    private int contentViewResId;

    public LoadingView(Context context) {
        super(context);
        init(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
            contentViewResId = typedArray.getResourceId(R.styleable.LoadingView_contentView, 0);
            typedArray.recycle();
        }
    }

    public View setContentView(int contentViewResId) {
        if(contentViewResId != 0) {
            return setContentView(LayoutInflater.from(getContext()).inflate(contentViewResId, this, false));
        }
        return null;
    }

    private View setContentView(View newContentView) {
        if (contentView != null) {
            removeView(contentView);
        }
        this.contentView = newContentView;
        addView(newContentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        notifyChildrenIndexChanged();
        return contentView;
    }

    protected void notifyChildrenIndexChanged() {
        indexOfLoading = indexOfChild(loadingView);
        indexOfContent = indexOfChild(contentView);
    }

    public void displayLoadingView() {
        ThreadingUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                setDisplayedChild(indexOfLoading);
            }
        });
    }

    public void displayContentView() {
        ThreadingUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                setDisplayedChild(indexOfContent);
            }
        });
    }

    public View getContentView() {
        return contentView;
    }

    public View getLoadingView() {
        return loadingView;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if(getChildCount() == 1) {
            contentView = getChildAt(0);
        }
        boolean hasContentView = (contentView != null);

        if(!isInEditMode()) {
            loadingView = LayoutInflater.from(getContext()).inflate(R.layout.view_loading_view_progress_view, this, false);
        }

        if(!hasContentView && setContentView(contentViewResId) == null) {
            setContentView(R.layout.view_loading_view_default_content_view);
        }

        LayoutParams params = this.generateDefaultLayoutParams();
        params.gravity = Gravity.CENTER;
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = LayoutParams.WRAP_CONTENT;

        if(!isInEditMode()) {
            this.addView(loadingView, params);
        }
        notifyChildrenIndexChanged();
    }

}
