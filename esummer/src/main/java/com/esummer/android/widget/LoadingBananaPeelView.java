package com.esummer.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.esummer.android.R;
import com.raizlabs.coreutils.threading.ThreadingUtils;

/**
 * Created by cwj on 16/8/10.
 */
public class LoadingBananaPeelView extends LoadingView {

    public interface BananaPeelListener {

        void onClickBananaPeel();

    }

    private View bananaPeelFooterView;
    private ListView contentView;

    private int bananaPeelViewIndex;
    private View bananaPeelView;

    private boolean hasBananaPeelAttr;

    private int bananaPeelFooterRes;
    private int bananaPeelViewRes;
    private int defaultMessageStringRes;
    private int defaultImageRes;

    public LoadingBananaPeelView(Context context) {
        super(context);
        initAttrs(null);
    }

    public LoadingBananaPeelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        this.hasBananaPeelAttr = (attrs != null);

        if(attrs != null) {
            TypedArray typedArray = this.getContext().getApplicationContext().
                    obtainStyledAttributes(attrs, R.styleable.LoadingBananaPeelView);
            defaultMessageStringRes = typedArray.getResourceId(
                    R.styleable.LoadingBananaPeelView_bananaPeelDefaultMessageStringResource, 0);
            defaultImageRes = typedArray.getResourceId(
                    R.styleable.LoadingBananaPeelView_bananaPeelDefaultImageResource, 0);
            bananaPeelViewRes = typedArray.getResourceId(
                    R.styleable.LoadingBananaPeelView_bananaPeelViewResource, 0);
            bananaPeelFooterRes = typedArray.getResourceId(
                    R.styleable.LoadingBananaPeelView_bananaPeelFooter, 0);
            typedArray.recycle();
        }
    }

    private View createBananaPeelView(int layoutRes) {
        View view = LayoutInflater.from(this.getContext()).inflate(layoutRes, this, false);
        this.addView(view);
        notifyChildrenIndexChanged();
        return view;
    }

    @Override
    protected void notifyChildrenIndexChanged() {
        super.notifyChildrenIndexChanged();
        bananaPeelViewIndex = this.indexOfChild(bananaPeelView);
    }

    public void setBananaPeel(int bananaPeelTitleText, int bananaPeelDetailText) {
        if(bananaPeelView != null) {
            TextView bananaPeelMsgDetail = (TextView)bananaPeelView.findViewById(R.id.bananaPeel_TextView_Line2);
            TextView bananaPeelMsg = (TextView)bananaPeelView.findViewById(R.id.bananaPeel_TextView);
            if(bananaPeelMsgDetail != null && bananaPeelMsg != null) {
                if(bananaPeelTitleText != 0) {
                    bananaPeelMsg.setVisibility(VISIBLE);
                    bananaPeelMsg.setTextAppearance(getContext(), bananaPeelTitleText);
                } else {
                    bananaPeelMsg.setVisibility(GONE);
                }
                if(bananaPeelDetailText != 0) {
                    bananaPeelMsgDetail.setVisibility(VISIBLE);
                    bananaPeelMsgDetail.setTextAppearance(this.getContext(), bananaPeelDetailText);
                } else {
                    bananaPeelMsgDetail.setVisibility(GONE);
                }
            }
        }
    }

    public void bindBananaPeelView(int messageRes, int imageDrawableRes, BananaPeelListener listener) {
        this.setBananaPeelListener(listener);
        this.setBananaPeelImage(imageDrawableRes);
        this.setBananaPeelMessage(messageRes);
    }

    public void setBananaPeelMsgDetailVisibility(boolean isVisible) {
        if(bananaPeelView != null) {
            View bananaPeelMsgDetail = bananaPeelView.findViewById(R.id.bananaPeel_TextView_Line2);
            if(bananaPeelMsgDetail != null) {
                if(isVisible && bananaPeelMsgDetail.getVisibility() != VISIBLE) {
                    bananaPeelMsgDetail.setVisibility(VISIBLE);
                }
                if(!isVisible && bananaPeelMsgDetail.getVisibility() == VISIBLE) {
                    bananaPeelMsgDetail.setVisibility(GONE);
                }
            }
        }
    }

    public void setBananaPeelViewCardStyle() {
        if(bananaPeelView != null) {
            CardView bananaPeelRootView = (CardView)bananaPeelView.findViewById(R.id.bananaPeel_cardView);
            if(bananaPeelRootView != null) {
                bananaPeelRootView.setCardBackgroundColor(-1);
                bananaPeelRootView.setUseCompatPadding(false);
                bananaPeelRootView.setCardElevation(0.0F);
                bananaPeelRootView.setRadius(0.0F);
                bananaPeelRootView.setContentPadding(0, 0, 0, 0);
                return;
            }
        }
    }

    public void displayBananaPeelView() {
        ThreadingUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                setDisplayedChild(bananaPeelViewIndex);
            }
        });
    }

    public boolean isDisplayBananaPeelView() {
        return this.getDisplayedChild() == bananaPeelViewIndex;
    }

    public void removeBananaPeelFooter() {
        if(this.bananaPeelFooterView != null && this.contentView != null) {
            contentView.removeFooterView(bananaPeelFooterView);
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        if(hasBananaPeelAttr) {
            if(this.bananaPeelFooterRes != 0 && this.getContentView() instanceof ListView) {
                bananaPeelFooterView = inflate(this.getContext(), this.bananaPeelFooterRes, null);
                contentView = (ListView)this.getContentView();
                contentView.addFooterView(bananaPeelFooterView, null, false);
            }

            if(bananaPeelViewRes != 0) {
                bananaPeelView = createBananaPeelView(bananaPeelViewRes);
            } else {
                bananaPeelView = createBananaPeelView(R.layout.view_default_banana_peel);
            }

            this.setBananaPeelMessage(defaultMessageStringRes);
            this.setBananaPeelImage(defaultImageRes);
        } else {
            bananaPeelView = createBananaPeelView(R.layout.view_default_banana_peel);
        }
        notifyChildrenIndexChanged();
    }

    public void setBananaPeelImage(int imageDrawableRes) {
        if(bananaPeelView != null) {
            ImageView bananaPeelImage = (ImageView)bananaPeelView.findViewById(R.id.bananaPeel_ImageView);
            if(bananaPeelImage != null) {
                if(imageDrawableRes == 0) {
                    bananaPeelImage.setVisibility(GONE);
                } else {
                    bananaPeelImage.setVisibility(VISIBLE);
                    bananaPeelImage.setImageDrawable(getResources().getDrawable(imageDrawableRes));
                    if(!this.isInEditMode()) {
                        int padding = (int)this.getResources().getDimension(R.dimen.Padding_VeryLarge);
                        bananaPeelImage.setPadding(padding, padding, padding, padding);
                    }
                }
            }
        }
    }

    public void setBananaPeelLayout(int layoutRes) {
        this.createBananaPeelView(layoutRes);
    }

    public void setBananaPeelListener(final BananaPeelListener listener) {
        bananaPeelView.setClickable(true);
        bananaPeelView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickBananaPeel();
                }
            }
        });
    }

    public void setBananaPeelMessage(int messageRes) {
        if(bananaPeelView != null) {
            TextView bananaPeelMsg = (TextView)bananaPeelView.findViewById(R.id.bananaPeel_TextView);
            if(bananaPeelMsg != null) {
                if(messageRes != 0) {
                    bananaPeelMsg.setVisibility(VISIBLE);
                    bananaPeelMsg.setText(messageRes);
                } else {
                    bananaPeelMsg.setVisibility(GONE);
                }
            }
        }
    }

    public void setBananaPeelMessage(String message) {
        if(bananaPeelView != null) {
            TextView bananaPeelTitle = (TextView)bananaPeelView.findViewById(R.id.bananaPeel_TextView);
            if(bananaPeelTitle != null) {
                bananaPeelTitle.setText(message);
            }
        }
    }

    public void setBananaPeelMessageDetail(int messageDetailRes) {
        if(bananaPeelView != null) {
            TextView bananaPeelMsgDetail = (TextView)bananaPeelView.findViewById(R.id.bananaPeel_TextView_Line2);
            if(bananaPeelMsgDetail != null) {
                if(messageDetailRes == 0) {
                    bananaPeelMsgDetail.setVisibility(GONE);
                } else {
                    bananaPeelMsgDetail.setVisibility(VISIBLE);
                    bananaPeelMsgDetail.setText(messageDetailRes);
                }
            }
        }
    }

    public void setBananaPeelMessageDetail(String messageDetail) {
        if(bananaPeelView != null) {
            TextView bananaPeelMsgDetail = (TextView)bananaPeelView.findViewById(R.id.bananaPeel_TextView_Line2);
            if(bananaPeelMsgDetail != null) {
                bananaPeelMsgDetail.setText(messageDetail);
            }
        }
    }
}
