package com.esummer.android.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.esummer.android.dialog.UniversalDialog.ActionListener;
import com.esummer.android.dialog.UniversalDialog.DialogBuilder;
import com.raizlabs.coreutils.functions.Delegate;

/**
 * Created by cwj on 16/7/7.
 */
public class DialogManager extends Fragment implements ActionListener {
    private SparseArray<DialogState> sparseArray;

    public static DialogManager newInstance() {
        return new DialogManager();
    }

    private boolean execute(Delegate<UniversalDialog> callback, UniversalDialog dialog) {
        if (callback != null) {
            callback.execute(dialog);
            return true;
        }
        return false;
    }

    protected DialogState getSavedState(int dialogId) {
        return sparseArray.get(dialogId);
    }

    public boolean containsDialog(DialogBuilder dialogBuilder) {
        return containsDialog(dialogBuilder.getDialogId());
    }

    public boolean containsDialog(int dialogId) {
        return getSavedDialog(dialogId) != null;
    }

    public UniversalDialog getSavedDialog(int dialogId) {
        DialogState state = getSavedState(dialogId);
        return (state != null) ? state.dialog : null;
    }

    public void bindDialogListener(DialogBuilder dialogBuilder) {
        DialogState savedState = getSavedState(dialogBuilder.getDialogId());
        DialogState newState = new DialogState();
        newState.dialogId = dialogBuilder.getDialogId();
        newState.negativeListener = dialogBuilder.getNegativeListener();
        newState.neutralListener = dialogBuilder.getNeutralListener();
        newState.positiveListener = dialogBuilder.getPositiveListener();
        newState.dismissListener = dialogBuilder.getDismissListener();
        if (savedState != null) {
            newState.dialog = savedState.dialog;
        }
        sparseArray.put(dialogBuilder.getDialogId(), newState);
    }

    public void unbindDialogListeners(DialogBuilder dialogBuilder) {
        DialogState savedState = getSavedState(dialogBuilder.getDialogId());
        if(savedState != null) {
            if(savedState.neutralListener == dialogBuilder.getNeutralListener()) {
                savedState.neutralListener = null;
            }
            if(savedState.negativeListener == dialogBuilder.getNegativeListener()) {
                savedState.negativeListener = null;
            }
            if(savedState.positiveListener == dialogBuilder.getPositiveListener()) {
                savedState.positiveListener = null;
            }
            if(savedState.dismissListener == dialogBuilder.getDismissListener()) {
                savedState.dismissListener = null;
            }
        }
    }

    @Override
    public void onNegative(UniversalDialog dialog) {
        DialogState dialogState = getSavedState(dialog.getDialogId());
        if (dialogState != null) {
            this.execute(dialogState.negativeListener, dialog);
        }
    }

    @Override
    public void onNeutral(UniversalDialog dialog) {
        DialogState dialogState = getSavedState(dialog.getDialogId());
        if (dialogState != null) {
            this.execute(dialogState.neutralListener, dialog);
        }
    }

    @Override
    public void onPositive(UniversalDialog dialog) {
        DialogState dialogState = getSavedState(dialog.getDialogId());
        if (dialogState != null) {
            this.execute(dialogState.positiveListener, dialog);
        }
    }

    @Override
    public void onDismiss(UniversalDialog dialog) {
        DialogState dialogState = getSavedState(dialog.getDialogId());
        if (dialogState != null) {
            dialogState.dialog = null;
        }
        sparseArray.remove(dialog.getDialogId());
    }

    @Override
    public void onCancel(UniversalDialog dialog) {
        DialogState dialogState = getSavedState(dialog.getDialogId());
        if (dialogState != null) {
            this.execute(dialogState.dismissListener, dialog);
        }
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * Call after on {@link DialogManager#bindDialogListener(DialogBuilder)}
     * if the old dialog is invalid, the dialog listener too.
     * @param dialog
     */
    public void bindDialog(UniversalDialog dialog) {
        DialogState savedState = this.getSavedState(dialog.getDialogId());
        DialogState newState = savedState;
        if(savedState == null) {
            newState = new DialogState();
            newState.dialogId = dialog.getDialogId();
        }
        newState.dialog = dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sparseArray = new SparseArray<>();
        this.setRetainInstance(true);
    }
}
