package com.esummer.android.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esummer.android.R;
import com.esummer.android.utils.ViewUtils;

/**
 * Created by cwj on 16/7/7.
 */
public class DefaultDialog extends UniversalDialog {
    private String title;
    private String message;
    private int style;

    private String negativeText;
    private int negativeTextColorResId;
    private boolean negativeDismiss;

    private String neutralText;
    private int neutralTextColorResId;
    private boolean neutralDismiss;

    private String positiveText;
    private int positiveTextColorResId;
    private boolean positiveDismiss;

    private boolean footerDivider;
    private boolean cancelable;

    private DefaultDialogViewBinder binder;

    public static DefaultDialog newInstance(DefaultDialogBuilder builder) {
        DefaultDialog dialog = new DefaultDialog();
        dialog.setDialogId(builder.getDialogId());
        Bundle bundle = dialog.getArguments();
        bundle.putString("title", builder.title);
        bundle.putString("message", builder.message);
        bundle.putInt("style", builder.style);
        bundle.putBoolean("cancelable", builder.cancelable);
        bundle.putBoolean("footerDivider", builder.footerDivider);
        if (builder.negativeBtn != null) {
            bundle.putString("negativeText", builder.negativeBtn.text);
            bundle.putInt("negativeTextColor", builder.negativeBtn.textColorResId);
            bundle.putBoolean("negativeDismiss", builder.negativeBtn.isDismiss);
        }
        if (builder.neutralBtn != null) {
            bundle.putString("neutralText", builder.neutralBtn.text);
            bundle.putInt("neutralTextColor", builder.neutralBtn.textColorResId);
            bundle.putBoolean("neutralDismiss", builder.neutralBtn.isDismiss);
        }
        if (builder.positiveBtn != null) {
            bundle.putString("positiveText", builder.positiveBtn.text);
            bundle.putInt("positiveTextColor", builder.positiveBtn.textColorResId);
            bundle.putBoolean("positiveDismiss", builder.positiveBtn.isDismiss);
        }
        dialog.init();
        return dialog;
    }

    @Override
    protected void init() {
        super.init();
        Bundle bundle = getArguments();
        title = bundle.getString("title");
        message = bundle.getString("message");
        style = bundle.getInt("style");
        cancelable = bundle.getBoolean("cancelable");
        footerDivider = bundle.getBoolean("footerDivider", true);

        negativeText = bundle.getString("negativeText");
        negativeTextColorResId = bundle.getInt("negativeTextColor");
        negativeDismiss = bundle.getBoolean("negativeDismiss", true);

        neutralText = bundle.getString("neutralText");
        neutralTextColorResId = bundle.getInt("neutralTextColor");
        neutralDismiss = bundle.getBoolean("neutralDismiss", true);

        positiveText = bundle.getString("positiveText");
        positiveTextColorResId = bundle.getInt("positiveTextColor");
        positiveDismiss = bundle.getBoolean("positiveDismiss");
    }

    private void setTextColor(TextView textView, int colorResId) {
        if (colorResId != 0) {
            textView.setTextColor(this.getResources().getColor(colorResId));
        }
    }

    private boolean setText(TextView textView, String text) {
        if (!TextUtils.isEmpty(text)) {
            textView.setText(Html.fromHtml(text));
            return true;
        } else {
            textView.setVisibility(View.GONE);
            return false;
        }
    }

    public void setTitle(String title) {
        this.title = title;
        if (binder != null && binder.titleView != null) {
            setText(binder.titleView, title);
        }
    }

    public void setMessage(String message) {
        this.message = message;
        if (binder != null && binder.messageView != null) {
            setText(binder.messageView, message);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binder = new DefaultDialogViewBinder();
        binder.bind(view);
        setTitle(title);
        setMessage(message);
        setCancelable(cancelable);
        setText(binder.negative, negativeText);
        setTextColor(binder.negative, negativeTextColorResId);
        setText(binder.neutral, neutralText);
        setTextColor(binder.neutral, neutralTextColorResId);
        setText(binder.positive, positiveText);
        setTextColor(binder.positive, positiveTextColorResId);
        if (style == STYLE_PROGRESSBAR) {
            ViewGroup.LayoutParams params = binder.contentScrollView.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            binder.contentScrollView.setLayoutParams(params);
            binder.textHolder.setPadding(24, 0, 24, 0);
            binder.footer.setVisibility(View.GONE);
        } else {
            binder.progressBar.setVisibility(View.GONE);
        }

        if (!footerDivider) {
            ViewUtils.setBackground(binder.footer, null);
        } else {
            ViewUtils.setBackground(binder.footer, getResources().getDrawable(R.drawable
                    .bg_note_footer));
        }

        binder.negative.setOnClickListener(new NegativeListener(this));
        binder.neutral.setOnClickListener(new NeutralListener(this));
        binder.positive.setOnClickListener(new PositiveListener(this));
    }

    static class NegativeListener implements View.OnClickListener {
        private DefaultDialog dialog;

        public NegativeListener(DefaultDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {
            ActionListener listener = dialog.getDialogListener();
            listener.onNegative(dialog);
            if (dialog.negativeDismiss) {
                dialog.dismiss();
            }
        }
    }

    static class NeutralListener implements View.OnClickListener {
        private DefaultDialog dialog;

        public NeutralListener(DefaultDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {
            ActionListener listener = dialog.getDialogListener();
            listener.onNeutral(dialog);
            if (dialog.neutralDismiss) {
                dialog.dismiss();
            }
        }
    }

    static class PositiveListener implements View.OnClickListener {
        private DefaultDialog dialog;

        public PositiveListener(DefaultDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {
            ActionListener listener = dialog.getDialogListener();
            if (listener != null) {
                listener.onPositive(dialog);
            }
            if (dialog.positiveDismiss) {
                dialog.dismiss();
            }
        }
    }
}
