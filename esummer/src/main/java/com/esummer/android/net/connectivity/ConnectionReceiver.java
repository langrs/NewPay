package com.esummer.android.net.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by cwj on 16/3/5.
 */
public class ConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectionManager.getInstance().stateChanged(ConnectionUtil.isConnectedOrConnecting(context));
    }

}
