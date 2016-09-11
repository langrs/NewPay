package com.esummer.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.esummer.android.dialog.DialogManager;
import com.esummer.android.dialog.DialogStateListener;
import com.esummer.android.dialog.UniversalDialog;
import com.esummer.android.dialog.UniversalDialog.ActionListener;
import com.esummer.android.dialog.UniversalDialog.DialogBuilder;
import com.esummer.android.uistate.UIState;
import com.esummer.android.uistate.UIStateStore;
import com.raizlabs.coreutils.app.FragmentStackManagerFragment;
import com.raizlabs.coreutils.threading.ThreadingUtils;

/**
 * Created by cwj on 16/7/6.
 */
public class StateManager implements ActionListener, DialogStateListener {
    private DialogManager dialogStateManager;
    private UIStateStore uiStateStore;
    private FragmentActivity bindActivity;

    public StateManager(FragmentActivity bindActivity) {
        this.bindActivity = bindActivity;
    }

    public static FragmentStackManagerFragment getFragmentStackManager(
            FragmentActivity activity, int contentId) {
        String stackTag = getStackTag(contentId);
        FragmentStackManagerFragment stackManager =
                (FragmentStackManagerFragment) activity.getSupportFragmentManager()
                .findFragmentByTag(stackTag);
        if (stackManager == null) {
            stackManager = FragmentStackManagerFragment.newInstance(contentId);
        }
        activity.getSupportFragmentManager().beginTransaction().add(stackManager, stackTag).commit();
        return stackManager;
    }


    private String getDialogTag(int dialogId) {
        return "Dialog:" + dialogId;
    }

    private static String getStackTag(int contentId) {
        return String.format("StackManager%d", contentId);
    }

    protected String getDialogTag(UniversalDialog dialog) {
        return dialog != null ? getDialogTag(dialog.getDialogId()) : "";
    }

    protected String getDialogTag(DialogBuilder dialogBuilder) {
        return dialogBuilder != null ? getDialogTag(dialogBuilder.getDialogId()) : "";
    }

    protected FragmentManager getFM() {
        return bindActivity.getSupportFragmentManager();
    }

    public FragmentStackManagerFragment getFragmentStackManager(int stackId) {
        return getFragmentStackManager(bindActivity, stackId);
    }

    public void initStacks(Bundle bundle) {
        this.dialogStateManager = (DialogManager) getFM().findFragmentByTag("DialogManager");
        if (this.dialogStateManager == null) {
            this.dialogStateManager = DialogManager.newInstance();
            getFM().beginTransaction().add(this.dialogStateManager, "DialogManager").commit();
            getFM().executePendingTransactions();
        }

        this.uiStateStore = (UIStateStore) getFM().findFragmentByTag("StateDataStore");
        if(this.uiStateStore == null) {
            this.uiStateStore = new UIStateStore();
            getFM().beginTransaction().add(this.uiStateStore, "StateDataStore").commit();
            getFM().executePendingTransactions();
        }
    }

    @Override
    public void onNegative(UniversalDialog dialog) {
        this.dialogStateManager.onNegative(dialog);
    }

    @Override
    public void bindActionListener(DialogBuilder dialogBuilder) {
        if (dialogBuilder != null) {
            this.dialogStateManager.bindDialogListener(dialogBuilder);
        }
    }

    public void showToast(final String msg) {
        ThreadingUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(bindActivity.getApplicationContext(),
                        msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void showToastLong(final String msg) {
        ThreadingUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(bindActivity.getApplicationContext(),
                        msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onDestroy() {
        if (bindActivity.isFinishing()) {
            this.uiStateStore.getRetainData().removeAll();
        }
    }

    public void showToast(int msgStrId) {
        showToast(bindActivity.getString(msgStrId));
    }

    @Override
    public void onNeutral(UniversalDialog dialog) {
        this.dialogStateManager.onNeutral(dialog);
    }

    @Override
    public void showDialog(DialogBuilder dialogBuilder) {
        if(dialogBuilder != null) {
            this.dialogStateManager.bindDialogListener(dialogBuilder);
            if(!this.dialogStateManager.containsDialog(dialogBuilder)) {
                this.showDialog(dialogBuilder.buildDialog());
            }
        }
    }

    public UIState<String, Object> getUIState() {
        return this.uiStateStore.getRetainData();
    }

    @Override
    public void onPositive(UniversalDialog dialog) {
        this.dialogStateManager.onPositive(dialog);
    }

    @Override
    public boolean dismissDialog(DialogBuilder dialogBuilder) {
        if (dialogBuilder != null) {
            String dialogTag = getDialogTag(dialogBuilder);
            Fragment fragment = getFM().findFragmentByTag(dialogTag);
            if (fragment != null && fragment instanceof UniversalDialog) {
                final UniversalDialog dialog = (UniversalDialog) fragment;
                dialog.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });
                return true;
            }
        }
        return false;
    }

    @Override
    public void clearActionListener(DialogBuilder dialogBuilder) {
        if (dialogBuilder != null) {
            this.dialogStateManager.unbindDialogListeners(dialogBuilder);
        }
    }

    @Override
    public void onDismiss(UniversalDialog dialog) {
        this.dialogStateManager.onDismiss(dialog);
    }

    @Override
    public void onCancel(UniversalDialog dialog) {
        this.dialogStateManager.onCancel(dialog);
    }


    public UniversalDialog getSavedDialog(int dialogId) {
        return dialogStateManager.getSavedDialog(dialogId);
    }

    protected void showDialog(UniversalDialog dialog) {
        if(dialog != null) {
            FragmentTransaction fm = getFM().beginTransaction();
            fm.add(dialog, getDialogTag(dialog));
            fm.commitAllowingStateLoss();
            this.dialogStateManager.bindDialog(dialog);
        }
    }
}
