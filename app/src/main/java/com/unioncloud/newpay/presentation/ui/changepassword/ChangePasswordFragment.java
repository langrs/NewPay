package com.unioncloud.newpay.presentation.ui.changepassword;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.login.ChangePassword;
import com.unioncloud.newpay.presentation.presenter.login.ChangedPasswordHandler;

/**
 * Created by cwj on 16/9/5.
 */
public class ChangePasswordFragment extends StatedFragment {

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        return fragment;
    }

    private static StateUpdateHandlerListener<ChangePasswordFragment, ChangedPasswordHandler> handlerListener =
            new StateUpdateHandlerListener<ChangePasswordFragment, ChangedPasswordHandler>() {
                @Override
                public void onUpdate(String key, ChangePasswordFragment handler, ChangedPasswordHandler response) {
                    handler.dealChangePassword(response);
                }

                @Override
                public void onCleanup(String key, ChangePasswordFragment handler, ChangedPasswordHandler response) {
                    response.removeCompletionListener(handler.changePasswordListener);
                }
            };
    private UpdateCompleteCallback<ChangedPasswordHandler> changePasswordListener =
            new UpdateCompleteCallback<ChangedPasswordHandler>() {
                @Override
                public void onCompleted(ChangedPasswordHandler response, boolean isSuccess) {
                    dealChangePassword(response);
                }
            };

    private EditText shopEt;
    private EditText userEt;
    private EditText oldPasswordEt;
    private EditText newPasswordEt;
    private EditText newPasswordConfirmEt;
    private Button okBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shopEt = (EditText) view.findViewById(R.id.change_password_shop);
        userEt = (EditText) view.findViewById(R.id.change_password_user);
        oldPasswordEt = (EditText) view.findViewById(R.id.change_password_oldPw);
        newPasswordEt = (EditText) view.findViewById(R.id.change_password_newPw);
        newPasswordConfirmEt = (EditText) view.findViewById(R.id.change_password_newPw_confirm);
        okBtn = (Button) view.findViewById(R.id.change_password_button);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String shopId = shopEt.getText().toString();
        if (TextUtils.isEmpty(shopId)) {
            showToast("分店编号不能为空");
            return;
        }
        String userId = userEt.getText().toString();
        if (TextUtils.isEmpty(userId)) {
            showToast("用户帐号不能为空");
            return;
        }
        String oldPassword = oldPasswordEt.getText().toString();
        if (TextUtils.isEmpty(userId)) {
            showToast("请输入旧密码");
            return;
        }
        String newPassword = newPasswordEt.getText().toString();
        if (TextUtils.isEmpty(userId)) {
            showToast("请输入新密码");
            return;
        }
        String newPasswordConfirm = newPasswordConfirmEt.getText().toString();
        if (TextUtils.isEmpty(userId)) {
            showToast("请再次确认新密码");
            return;
        }
        if (!newPassword.equals(newPasswordConfirm)) {
            showToast("2次新密码不一致!");
        }

        ChangePassword cp = new ChangePassword();
        cp.setShopId(shopId);
        cp.setUserId(userId);
        cp.setOldPassword(oldPassword);
        cp.setNewPassword(newPassword);
        ChangedPasswordHandler handler = new ChangedPasswordHandler(cp);
        updateForResponse("ChangePasswordFragment:changePw", handler, handlerListener);
        handler.run();
    }

    private void dealChangePassword(ChangedPasswordHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("修改密码...");
                handler.addCompletionListener(changePasswordListener);
            } else {
                if (handler.isSuccess() && handler.getData()) {
                    getActivity().finish();
                }
                dismissProgressDialog();
                cleanupResponse("ChangePasswordFragment:changePw");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
