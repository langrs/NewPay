package com.esummer.android.dialog;

import com.esummer.android.dialog.UniversalDialog.DialogBuilder;
import com.esummer.android.fragment.StateManagerProvider;

/**
 * Created by cwj on 16/7/11.
 */
public class DialogStateHelper {

    private DialogStateListener dialogStateListener;

    public DialogStateHelper(StateManagerProvider provider) {
        dialogStateListener = provider.getStateManager();
    }

    public void bindActionListener(DialogBuilder dialogBuilder) {
        if ((dialogBuilder != null) && (this.dialogStateListener != null)) {
            dialogStateListener.bindActionListener(dialogBuilder);
        }
    }

    public void showDialog(DialogBuilder dialogBuilder) {
        if ((dialogBuilder != null) && (this.dialogStateListener != null)) {
            this.dialogStateListener.showDialog(dialogBuilder);
        }
    }

    public boolean dismissDialog(DialogBuilder dialogBuilder) {
        if ((dialogBuilder != null) && (this.dialogStateListener != null)) {
            return this.dialogStateListener.dismissDialog(dialogBuilder);
        }
        return false;
    }

    public void unbindActionListener(DialogBuilder dialogBuilder) {
        if ((dialogBuilder != null) && (this.dialogStateListener != null)) {
            this.dialogStateListener.clearActionListener(dialogBuilder);
        }
    }
}
