package com.esummer.android.activity;

import android.content.Context;
import android.content.res.Resources;

import com.esummer.android.R;
import com.esummer.android.dialog.DefaultDialogBuilder;
import com.esummer.android.dialog.DialogStateListener;
import com.esummer.android.dialog.UniversalDialog;
import com.esummer.android.net.connectivity.ConnectionManager;
import com.esummer.android.net.connectivity.ConnectionManager.ConnectionStateListener;

/**
 * Created by cwj on 16/7/7.
 */
public class ConnectionDialogUtils {
    private static final int DIALOG_ID = UniversalDialog.getAutoId();

    DefaultDialogBuilder dialogBuilder;
    private ConnectionStateListener connectionListener;
    private DialogStateListener dialogListener;
    private Context context;

    public ConnectionDialogUtils(Context context, DialogStateListener listener) {
        this.context = context.getApplicationContext();
        this.dialogListener = listener;
    }

    public void registerListener() {
        ConnectionManager.getInstance().registerListener(connectionListener, context);
    }

    public void removeListener() {
        ConnectionManager.getInstance().unregisterListener(connectionListener, context);
    }

    public void init(Resources res) {
        dialogBuilder = new DefaultDialogBuilder(DIALOG_ID)
                .setTitle(context.getString(R.string.Dialog_ErrorTitle_InternetConnection))
                .setMessage(context.getString(R.string.Dialog_DefaultErrorMessage_InternetConnection))
                .setStyle(0);
        dialogBuilder.setNeutralButton("", null, R.color.Dialog_ContentColor);
        if (dialogListener != null) {
            dialogListener.bindActionListener(dialogBuilder);
        }
        connectionListener = new ConnectionStateListener() {
            @Override
            public void onStateChanged(boolean isConnecting) {
                if (!isConnecting && dialogListener != null) {
                    dialogListener.showDialog(dialogBuilder);
                }
            }
        };
    }

}
