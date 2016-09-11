package com.esummer.android.net.connectivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

/**
 * This class provides service that listen to the connection state and call you.
 */
public class ConnectionManager {

    /**
     * Interface definition for a callback to be invoked when the state of connection changed
     */
    public interface ConnectionStateListener {

        /**
         * Called when the state of connection changed
         * @param isConnecting true if net is connecting, false otherwise
         */
        void onStateChanged(boolean isConnecting);
    }

    private static final ConnectionManager INSTANCE = new ConnectionManager();

    private ConnectionReceiver receiver = new ConnectionReceiver();

    private boolean isConnecting;

    private ConnectionStateListener listener;

    public static ConnectionManager getInstance() {
        return INSTANCE;
    }

    private void register(Context context) {
        context.registerReceiver(this.receiver, new IntentFilter(), "com.common.android.net.CONNECTION_MONITORING", null);
        ComponentName componentName = new ComponentName(context, ConnectionReceiver.class);
        // 设置应用程序组件不要被系统kill
        context.getPackageManager().setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    private void unregister(Context context) {
        context.unregisterReceiver(this.receiver);
        ComponentName componentName = new ComponentName(context, ConnectionReceiver.class);
        // 设置应用程序组件不要被系统kill
        context.getPackageManager().setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    /**
     * Register a callback to be invoked when the connection state changed
     * @param listener
     * @param context
     */
    public void registerListener(ConnectionStateListener listener, Context context) {
        if(listener != null) {
            this.listener = listener;
            register(context);
        }
    }

    /**
     * Unregister a previously registered ConnectionStateListener
     * @param listener
     * @param context
     */
    public void unregisterListener(ConnectionStateListener listener, Context context) {
        if(this.listener == listener) {
            this.listener = null;
            unregister(context);
        }
    }

    void stateChanged(boolean isConnecting) {
        if((this.isConnecting != isConnecting) && (this.listener != null)) {
            this.listener.onStateChanged(isConnecting);
        }
        this.isConnecting = isConnecting;
    }

}
