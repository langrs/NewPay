package com.esummer.android.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Window;

import com.esummer.android.uistate.UIState;
import com.raizlabs.coreutils.functions.Delegate;

/**
 * Created by cwj on 16/6/28.
 */
public class BaseFragment extends DialogFragment {
    private Delegate<BaseFragment> dismissListener;

    UIState<String, Object> retain;

    public BaseFragment() {
        setArguments(new Bundle());
    }

    protected boolean saveState(String key, String data) {
        if (retain != null) {
            retain.saveState(key, data);
            return true;
        }
        return false;
    }

    public void setDismissListener(Delegate<BaseFragment> listener) {
        this.dismissListener = listener;
    }

    protected Object getSaved(String key) {
        return retain == null ? null : retain.getState(key);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity() instanceof StateManagerProvider) {
            StateManager stateManager = ((StateManagerProvider) getActivity()).getStateManager();
            if (stateManager != null) {
                retain = stateManager.getUIState();
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroy() {
        this.retain = null;
        super.onDestroy();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (this.dismissListener != null) {
            this.dismissListener.execute(this);
        }
    }
}
