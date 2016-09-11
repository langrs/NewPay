package com.esummer.android.dialog;

import android.text.Spanned;

import com.esummer.android.R;
import com.raizlabs.coreutils.functions.Delegate;

import com.esummer.android.dialog.UniversalDialog.DialogBuilder;

/**
 * Created by cwj on 16/7/7.
 */
public class DefaultDialogBuilder implements DialogBuilder {

    public static class ButtonHold {

        public String text;
        public int textColorResId;
        public boolean isDismiss;
        public Delegate<UniversalDialog> listener;

    }

    protected int dialogId;
    protected String title;
    protected String message;
    protected int style;
    protected boolean cancelable;
    protected boolean footerDivider;

    protected ButtonHold positiveBtn;
    protected ButtonHold neutralBtn;
    protected ButtonHold negativeBtn;
    protected Delegate<UniversalDialog> dismissListener;

    public DefaultDialogBuilder(int dialogId) {
        this.dialogId = dialogId;
        this.cancelable = true;
        setStyle(0);
    }

    public DefaultDialogBuilder setStyle(int style) {
        this.style = style;
        return this;
    }

    public DefaultDialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public DefaultDialogBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public DefaultDialogBuilder setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public DialogBuilder setNeutralButton(String text, Delegate<UniversalDialog> listener) {
        return setNeutralButton(text, listener, R.drawable.button_dialog_negative_text_color, true);
    }

    public DialogBuilder setNeutralButton(String text, Delegate<UniversalDialog> listener, int
            textColorResId) {
        return setNeutralButton(text, listener, textColorResId, true);
    }

    public DialogBuilder setNeutralButton(String text, Delegate<UniversalDialog> listener, int
            textColorResId, boolean isDismiss) {
        neutralBtn = createButtonHold(text, listener, textColorResId, isDismiss);
        return this;
    }

    public DialogBuilder setPositiveButton(String text, Delegate<UniversalDialog> listener) {
        return setPositiveButton(text, listener, R.drawable.button_dialog_positive_text_color, true);
    }


    public DialogBuilder setPositiveButton(String text, Delegate<UniversalDialog> listener, int
            textColorResId) {
        return setPositiveButton(text, listener, textColorResId, true);
    }

    public DialogBuilder setPositiveButton(String text, Delegate<UniversalDialog> listener, int
            textColorResId, boolean isDismiss) {
        positiveBtn = createButtonHold(text, listener, textColorResId, isDismiss);
        return this;
    }

    private ButtonHold createButtonHold(String text, Delegate<UniversalDialog> listener,
                                        int textColorResId, boolean isDismiss) {
        ButtonHold hold = new ButtonHold();
        hold.text = text;
        hold.listener = listener;
        hold.textColorResId = textColorResId;
        hold.isDismiss = isDismiss;
        return hold;
    }


    @Override
    public int getDialogId() {
        return dialogId;
    }

    @Override
    public Delegate<UniversalDialog> getPositiveListener() {
        return (positiveBtn == null) ? null : positiveBtn.listener;
    }

    @Override
    public Delegate<UniversalDialog> getNeutralListener() {
        return (neutralBtn == null) ? null : neutralBtn.listener;
    }

    @Override
    public Delegate<UniversalDialog> getNegativeListener() {
        return (negativeBtn == null) ? null : negativeBtn.listener;
    }

    @Override
    public Delegate<UniversalDialog> getDismissListener() {
        return dismissListener;
    }

    @Override
    public UniversalDialog buildDialog() {
        return DefaultDialog.newInstance(this);
    }
}
