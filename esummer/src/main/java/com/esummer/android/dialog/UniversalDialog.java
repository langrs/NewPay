package com.esummer.android.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.esummer.android.task.UITaskQueue;
import com.raizlabs.coreutils.functions.Delegate;

/**
 * Created by cwj on 16/7/6.
 */
public class UniversalDialog extends DialogFragment {

    public interface DialogBuilder {
        int getDialogId();

        Delegate<UniversalDialog> getPositiveListener();

        Delegate<UniversalDialog> getNeutralListener();

        Delegate<UniversalDialog> getNegativeListener();

        Delegate<UniversalDialog> getDismissListener();

        UniversalDialog buildDialog();
    }

    public interface ActionListener {

        void onNegative(UniversalDialog dialog);

        void onNeutral(UniversalDialog dialog);

        void onPositive(UniversalDialog dialog);

        void onDismiss(UniversalDialog dialog);

        void onCancel(UniversalDialog dialog);
    }

    private static int idSequence = Integer.MIN_VALUE;

    public static int STYLE_PROGRESSBAR = Integer.parseInt("110", 2);

    private UITaskQueue taskQueue;
    private ActionListener dialogListener;
    private int dialogId;

    public UniversalDialog() {
        setArguments(new Bundle());
    }

    public static int getAutoId() {
        int autoId = idSequence;
        idSequence += 1;
        return autoId;
    }

    protected void init() {
        this.setDialogId(getArguments().getInt("id"));
    }

    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
        getArguments().putInt("id", dialogId);
    }

    public final void post(Runnable action) {
        if(taskQueue != null) {
            taskQueue.execute(action);
        }
    }

    public ActionListener getDialogListener() {
        return dialogListener;
    }

    public int getDialogId() {
        return dialogId;
    }

    @Override
    public void dismiss() {
        if(dialogListener != null) {
            dialogListener.onDismiss(this);
        }
        super.dismissAllowingStateLoss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskQueue = new UITaskQueue();
        init();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof DialogListenerProvider) {
            dialogListener = ((DialogListenerProvider) getActivity()).provide();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        taskQueue.loop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dialogListener = null;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        dialogListener.onCancel(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        taskQueue.pause();
        super.onSaveInstanceState(outState);
    }
}
