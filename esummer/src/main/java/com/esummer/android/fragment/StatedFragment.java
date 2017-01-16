package com.esummer.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.esummer.android.FragmentStackDelegate;
import com.esummer.android.R;
import com.esummer.android.dialog.DefaultDialog;
import com.esummer.android.dialog.DefaultDialogBuilder;
import com.esummer.android.dialog.DialogStateHelper;
import com.esummer.android.dialog.PermissionDialog;
import com.esummer.android.dialog.UniversalDialog;
import com.esummer.android.dialog.UniversalDialog.DialogBuilder;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerManager;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.stateupdatehandler.UpdateHandlerManager;
import com.esummer.android.net.connectivity.ConnectionUtil;
import com.esummer.android.task.UITaskQueue;
import com.esummer.android.uistate.HashUIState;
import com.esummer.android.uistate.UIState;
import com.esummer.android.utils.IdSequenceUtils;
import com.raizlabs.coreutils.functions.Delegate;
import com.raizlabs.coreutils.threading.ThreadingUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cwj on 16/7/11.
 */
public class StatedFragment extends BaseFragment implements FragmentStack {

    private static final int PROGRESS_DIALOG_ID = UniversalDialog.getAutoId();

//    private r c;
    private FragmentStackDelegate fragmentStack;
//    private com.raizlabs.d.d<String, Object> e;
    private UIState<String, Object> savedState;

//    private com.raizlabs.p.a.b f;
    private UITaskQueue taskQueue;
//    private com.bhphoto.general.b.e g;
    private DefaultDialogBuilder progressDialogBuilder;
//    private SparseArray<com.bhphoto.general.b.e> i = new SparseArray();
    private SparseArray<DefaultDialogBuilder> dialogBuilderArray = new SparseArray<>();

    private Integer fragmentId;
    private boolean allowDismiss;

//    private com.raizlabs.a.a.a<e> l
    private UpdateHandlerManager<StatedFragment> updateHandler;

//    protected com.afollestad.materialdialogs.MaterialDialog materialDialog;
    protected DialogStateHelper dialogStateHelper;

//    private com.raizlabs.d.d<String, Object> b()
    private UIState<String, Object> getDefaultResponseDataManager() {
        return getResponseDataManager(getFragmentStateTag() + "ResponseManagerData");
    }

//    private String b(int paramInt)
    private String getChildRequestKey(int requestCode) {
        return "RequestCode:" + requestCode;
    }

//    private void d()
    private void setTitle() {
        String title = getArguments().getString("fragment_title");
        if ((title != null) && (getActivity() != null)) {
            getActivity().setTitle(title);
        }
    }

//    private boolean e()
    private boolean isShowing() {
        return (getActivity() != null) && (isAdded()) && (getView() != null);
    }

//    public void F()
    private void initUpdateQueue() {
        this.taskQueue = new UITaskQueue();
        this.updateHandler = new StateUpdateHandlerManager<>();
    }

//    protected String G()
    protected String getFragmentStateTag() {
        return "ESummerFrag" + getFragmentId();
    }

//    public boolean H()
//    public boolean dismissDialog() {
    public boolean onBackPressed() {
        if (getShowsDialog()) {
            dismissAllowingStateLoss();
            return true;
        }
        if (this.fragmentStack != null) {
            return this.fragmentStack.pop(this, this);
        }
        return false;
    }

    protected UniversalDialog getSavedDialog(int dialogId) {
        return getUIStateManager().getSavedDialog(dialogId);
    }

//    protected com.bhphoto.general.activities.l I()
    protected StateManager getUIStateManager() {
        if (getActivity() == null) {
            return null;
        }
        return ((StateManagerProvider)getActivity()).getStateManager();
    }

//    protected com.raizlabs.d.d<String, Object> K()
    protected UIState<String, Object> getSavedState() {
        StateManager stateManager = getUIStateManager();
        if (stateManager != null) {
            return stateManager.getUIState();
        }
        return null;
    }

//    public int L()
    public int getFragmentId() {
        if (this.fragmentId == null) {
            if (getArguments().containsKey("FragID")) {
                this.fragmentId = getArguments().getInt("FragID");
                IdSequenceUtils.updateSequence(fragmentId);
            } else {
                this.fragmentId = IdSequenceUtils.nextId();
                getArguments().putInt("FragID", fragmentId);
            }
        }
        return fragmentId;
    }

//    protected void O()
    protected void hideMaterialDialog() {
        ThreadingUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
//                materialDialog.hide();
            }
        });
    }

//    protected boolean P()
    protected boolean isConnecting() {
        return ConnectionUtil.isConnectedOrConnecting(getActivity().getApplicationContext());
    }

//    protected com.raizlabs.a.a.a<e> Q()
    protected UpdateHandlerManager<StatedFragment> getStateResponseManager() {
        return this.updateHandler;
    }

//    protected boolean R()
    protected boolean isUsedDepartment() {
//        return getBaseActivity().isUsedDepartment();
        return false;
    }

    //    protected void M()
    protected void showProgressDialog() {
        showProgressDialog(R.string.dialog_in_progress_general_message);
    }

    //    protected void N() {
    protected void dismissProgressDialog() {
        dismissDialog(progressDialogBuilder);
    }

    //    protected void g(int paramInt)
    protected void showProgressDialog(int stringResId) {
        showProgressDialog(getString(stringResId));
    }

    //    protected void j(String paramString)
    protected void showProgressDialog(final String message) {
        final DefaultDialog savedProgressDialog = (DefaultDialog) getSavedDialog(progressDialogBuilder.getDialogId());
        if (savedProgressDialog != null && savedProgressDialog.isAdded()) {
            ThreadingUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    savedProgressDialog.setMessage(message);
                }
            });
        } else {
            progressDialogBuilder.setMessage(message);
            showDialog(progressDialogBuilder);
        }
    }

    //    protected void a(int paramInt1, int paramInt2) {
    protected void showSimpleDialog(int dialogId, int messageResId) {
        showSimpleDialog(dialogId, getString(messageResId));
    }

//    protected void a(com.bhphoto.general.b.j paramj)
    protected void bindDialogActionListener(DialogBuilder dialogBuilder) {
        if ((dialogBuilder != null) && (this.dialogStateHelper != null)) {
            this.dialogStateHelper.bindActionListener(dialogBuilder);
        }
    }

//    protected void a(Runnable paramRunnable)
    protected void runOnUIThread(Runnable action) {
        if (this.taskQueue != null) {
            this.taskQueue.execute(action);
        }
    }

//    protected void a(String paramString, int dialogId)
    protected void requestPermission(String permission, int permissionMessageResId) {
        boolean idDenied = isPermissionDeniedPermanently(getActivity(), permission);
        boolean rational = !shouldShowRequestPermissionRationale(permission);
        if (rational && idDenied) {
            showPermissionDialog(permissionMessageResId);
        } else {
            setPermissionDeniedPermanently(getActivity(), permission, rational);
        }
    }

//    protected <HandlerType extends e, ResponseType> void a(String paramString, ResponseType paramResponseType, com.raizlabs.a.a.b<HandlerType, ResponseType> paramb)
    protected <HandlerType extends StatedFragment, ResponseType> void updateForResponse(
            String key, ResponseType responseType,
            StateUpdateHandlerListener<HandlerType, ResponseType> listener) {
        getStateResponseManager().updateForResponse(key, this, responseType, listener);
    }

//    protected void a(String paramString, List<String> paramList, boolean paramBoolean) {
    protected void showToast(String message) {
        if (!isShowing()) {
            return;
        }
        StateManager stateManager = getUIStateManager();
        if (stateManager != null) {
            stateManager.showToast(message);
        }
    }

    protected void showToastLong(String message) {
        if (!isShowing()) {
            return;
        }
        StateManager stateManager = getUIStateManager();
        if (stateManager != null) {
            stateManager.showToastLong(message);
        }
    }

//    protected boolean a(int paramInt1, int paramInt2, Intent paramIntent)
    protected boolean childOnActivityResult(int requestCode, int resultCode, Intent intent) {
        int fragmentId = containRequest(requestCode);
        if (fragmentId == 0) {
            return false;
        }
        removeChildRequest(requestCode);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments == null) {
            return false;
        }
        Iterator<Fragment> iterator = fragments.iterator();
        while (iterator.hasNext()) {
            Fragment fragment = iterator.next();
            if ((fragment instanceof StatedFragment) &&
                    (((StatedFragment)fragment).getFragmentId() == fragmentId)) {
                fragment.onActivityResult(requestCode, resultCode, intent);
                return true;
            }
        }
        return false;
    }

//    protected boolean a(String paramString, Object paramObject)
    protected boolean saveState(String key, Object value) {
        if (savedState != null) {
            savedState.saveState(getFragmentStateTag() + key, value);
            return true;
        }
        return false;
    }

//    protected void a_(int paramInt) {}
    protected void onDialogOkClicked(int dialogId) {
        // do nothing
    }

//    protected void b(int paramInt, String paramString)
    protected void showSimpleDialog(int dialogId, String message) {
        DefaultDialogBuilder builder = getSavedBuilder(dialogId);
        if (builder == null) {
            builder = createAndSaveDialogBuilder(dialogId);
        }
        if (message != null) {
            builder.setMessage(message);
        }
        showDialog(builder);
    }

//    protected void b(com.bhphoto.general.b.j paramj)
    protected void showDialog(final DialogBuilder dialogBuilder) {
        if (getView() != null) {
            ThreadingUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    dialogStateHelper.showDialog(dialogBuilder);
                }
            });
        }
    }

//    public boolean b(Fragment paramFragment)
    public boolean pushToStack(Fragment paramFragment) {
        if (this.fragmentStack != null) {
            return this.fragmentStack.push(this, paramFragment);
        }
        return false;
    }

//    public void c(int paramInt1, int paramInt2)
    public void saveChildRequest(int requestCode, int childFragmentId) {
        getArguments().putInt(getChildRequestKey(requestCode), childFragmentId);
    }

//    protected void c(com.bhphoto.general.b.j paramj)
    protected void dismissDialog(final DialogBuilder dialogBuilder) {
        if (getView() != null) {
            ThreadingUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    dialogStateHelper.dismissDialog(dialogBuilder);
                }
            });
        }
    }

//    protected Object d(String paramString)
    protected Object getSaved(String stateKey) {
        if (this.savedState != null) {
            return this.savedState.getState(getFragmentId() + stateKey);
        }
        return null;
    }

//    protected void d(com.bhphoto.general.b.j paramj)
    protected void unbindDialogListener(DialogBuilder dialogBuilder) {
        if ((dialogBuilder != null) && (this.dialogStateHelper != null)) {
            this.dialogStateHelper.unbindActionListener(dialogBuilder);
        }
    }

//    protected Object e(String paramString)
    protected Object getStateResponse(String key) {
        return getStateResponseManager().getResponse(key);
    }

//    public boolean e_()
    public boolean onResult() {     // 如果返回true, 表示该类有调用setResult
        return false;
    }

//    protected void f(int paramInt) {}
    protected void onDialogCancelClicked(int dialogId) {
        // do nothing
    }

//    protected void f(boolean paramBoolean)
    protected void setAllowDismiss(boolean allowDismiss) {
        this.allowDismiss = allowDismiss;
        getArguments().putBoolean("KEY_DISMISS_FRAGMENT", allowDismiss);
    }

//    protected boolean f(String paramString)
    protected boolean cleanupResponse(String key) {
        return getStateResponseManager().cleanupResponse(key, this);
    }

//    protected boolean g(String paramString)
    protected boolean updateForResponse(String key) {
        return getStateResponseManager().updateForResponse(key, this);
    }

//    protected com.raizlabs.d.d<String, Object> h(String paramString)
    protected UIState<String, Object> getResponseDataManager(String managerTag) {
        UIState<String, Object> savedState = getSavedState();
        if (savedState != null) {
            UIState<String, Object> responseDataManage =
                    (UIState<String, Object>) savedState.getState(managerTag);
            if (responseDataManage == null) {
                responseDataManage = new HashUIState<>();
                savedState.saveState(managerTag, responseDataManage);
            }
            return responseDataManage;
        }
        return null;
    }

    //    protected com.bhphoto.general.b.e i(int paramInt)
    protected DefaultDialogBuilder getSavedBuilder(int dialogId) {
        return dialogBuilderArray.get(dialogId);
    }

//    protected void i(String paramString)
    protected void setTitle(String title) {
        getArguments().putString("fragment_title", title);
        if (getActivity() != null) {
            setTitle();
        }
    }

//    protected com.bhphoto.general.b.e j(int paramInt)
    protected DefaultDialogBuilder createAndSaveDialogBuilder(final int dialogId) {
        DefaultDialogBuilder builder = new DefaultDialogBuilder(dialogId)
                .setTitle(getString(R.string.dialog_confirm_general_title))
                .setMessage(getString(R.string.dialog_confirm_general_message))
                .setStyle(0);
        builder.setPositiveButton(getString(R.string.dialog_button_title_ok), new Delegate<UniversalDialog>() {
            @Override
            public void execute(UniversalDialog dialog) {
                onDialogOkClicked(dialog.getDialogId());
            }
        });
        builder.setNeutralButton(getString(R.string.dialog_button_title_cancel), new Delegate<UniversalDialog>() {
            @Override
            public void execute(UniversalDialog dialog) {
                onDialogCancelClicked(dialog.getDialogId());
            }
        });
//        this.dialogBuilderArray.put(dialogId, builder);
//        bindDialogActionListener(builder);
        saveDialogBuilder(builder);

        return builder;
    }

    protected void saveDialogBuilder(DefaultDialogBuilder builder) {
        this.dialogBuilderArray.put(builder.getDialogId(), builder);
        bindDialogActionListener(builder);
    }

//    protected void k(int paramInt)
    protected void showToast(int messageResId) {
        if (isShowing()) {
            showToast(getString(messageResId));
        }
    }

//    protected boolean l(String paramString)
    protected boolean hasPermissionDenied(String permission) {
        return isPermissionDeniedPermanently(getActivity(), permission);
    }

//    public int m(int paramInt)
    public int containRequest(int requestCode) {
        return getArguments().getInt(getChildRequestKey(requestCode));
    }

//    public void n(int paramInt)
    public void removeChildRequest(int requestCode) {
        getArguments().remove(getChildRequestKey(requestCode));
    }

//    public void o(int paramInt)
    public void showPermissionDialog(int permissionMessageResId) {
        PermissionDialog.newInstance(permissionMessageResId).show(getChildFragmentManager(), "PermissionsDialog");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<Integer> builderSaveKeys = savedInstanceState.getIntegerArrayList("CONFIRM_BUILDER_SAVE_KEY");
            for (Integer key : builderSaveKeys) {
                createAndSaveDialogBuilder(key);
            }
        }
        StateManager stateManager = getUIStateManager();
        if (stateManager != null) {
            savedState = stateManager.getUIState();
        }
        dialogStateHelper = new DialogStateHelper((StateManagerProvider) getActivity());
        fragmentStack = (FragmentStackDelegate) getActivity();
        bindDialogActionListener(progressDialogBuilder);
        for (int i = 0, size = dialogBuilderArray.size(); i < size; i ++) {
            bindDialogActionListener(dialogBuilderArray.valueAt(i));
        }
        if (savedInstanceState != null) {
            updateHandler.updateForResponse(this, getDefaultResponseDataManager());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!childOnActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUpdateQueue();

        this.progressDialogBuilder = new DefaultDialogBuilder(PROGRESS_DIALOG_ID)
                .setMessage(getString(R.string.dialog_in_progress_general_message))
                .setStyle(UniversalDialog.STYLE_PROGRESSBAR)
                .setCancelable(false);
        this.allowDismiss = getArguments().getBoolean("KEY_DISMISS_FRAGMENT");
        setTitle();
    }

    @Override
    public void onDestroy() {
        this.savedState = null;
        super.onDestroy();
    }

    /** 是否持久化 */
    protected boolean retainUpdateHandler() {
        return false;
    }

    @Override
    public void onDestroyView() {
        if (!retainUpdateHandler()) {
            updateHandler.clearState(this);
        }
        unbindDialogListener(progressDialogBuilder);
        for (int i = 0, size = dialogBuilderArray.size(); i < size; i++) {
            unbindDialogListener(dialogBuilderArray.valueAt(i));
        }
        dialogBuilderArray.clear();
        this.dialogStateHelper = null;
        this.fragmentStack = null;
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        taskQueue.loop();
        if (this.allowDismiss) {
            dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        taskQueue.pause();
        ArrayList<Integer> builderStateList = new ArrayList<>();
        for (int i = 0, size = dialogBuilderArray.size(); i < size; i++) {
            builderStateList.add(dialogBuilderArray.keyAt(i));
        }
        outState.putIntegerArrayList("CONFIRM_BUILDER_SAVE_KEY", builderStateList);
        updateHandler.saveState(getDefaultResponseDataManager());
        super.onSaveInstanceState(outState);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        if ((getParentFragment() instanceof StatedFragment)) {
            StatedFragment parent = (StatedFragment) getParentFragment();
            parent.saveChildRequest(requestCode, getFragmentId());
            parent.startActivityForResult(intent, requestCode);
            return;
        }
//        getActivity().startActivityForResult(intent, requestCode);
        super.startActivityForResult(intent, requestCode);
    }

    public static void setPermissionDeniedPermanently(Context context, String permissionKey, boolean isDenied) {
        SharedPreferences.Editor edit = context.getSharedPreferences("PERMISSIONS_PREFERENCES ", 0).edit();
        edit.putBoolean(permissionKey, isDenied);
        edit.apply();
    }

    public static boolean isPermissionDeniedPermanently(Context context, String permissionKey) {
        return context.getSharedPreferences("PERMISSIONS_PREFERENCES ", 0).getBoolean(permissionKey, false);
    }

}
