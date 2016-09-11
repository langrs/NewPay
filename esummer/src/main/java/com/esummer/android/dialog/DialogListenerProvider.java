package com.esummer.android.dialog;

/**
 * if use BaseDialog, and you want listen the dialog action(Negative, Neutral, Positive, Dismiss,
 * Cancel), the host activity can implements this to provide listener.
 *
 * Created by cwj on 16/3/25.
 */
public interface DialogListenerProvider {

    UniversalDialog.ActionListener provide();

}
