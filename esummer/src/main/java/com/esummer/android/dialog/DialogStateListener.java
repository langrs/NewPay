package com.esummer.android.dialog;

import com.esummer.android.dialog.UniversalDialog.DialogBuilder;

/**
 * Created by cwj on 16/7/7.
 */
public interface DialogStateListener {

    /**
     * Call when the dialog action listener is changed
     * @param dialogBuilder
     */
    void bindActionListener(DialogBuilder dialogBuilder);

    /**
     * Call when the dialog is changed.
     * That means the action listener is also changed.
     * @param dialogBuilder
     */
    void showDialog(DialogBuilder dialogBuilder);

    /**
     *
     * @param dialogBuilder
     * @return
     */
    boolean dismissDialog(DialogBuilder dialogBuilder);

    /**
     *
     * @param dialogBuilder
     */
    void clearActionListener(DialogBuilder dialogBuilder);
}
