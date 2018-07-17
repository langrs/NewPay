package com.unioncloud.newpay.presentation.ui.login;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.esummer.android.utils.DeviceUtils;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.login.UserLogin;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.login.LoginHandler;
import com.unioncloud.newpay.presentation.presenter.login.QueryProductHandler;
import com.unioncloud.newpay.presentation.presenter.sharedpreferences.PreferencesUtils;
import com.unioncloud.newpay.presentation.ui.changepassword.ChangePasswordActivity;
import com.unioncloud.newpay.presentation.ui.placeorder.PlaceOrderActivity;
import com.unioncloud.newpay.presentation.ui.register.RegisterActivity;

/**
 * Created by cwj on 16/7/22.
 */
public class LoginFragment extends StatedFragment {

    private static StateUpdateHandlerListener<LoginFragment, LoginHandler> handlerListener =
            new StateUpdateHandlerListener<LoginFragment, LoginHandler>() {
                @Override
                public void onUpdate(String key, LoginFragment handler, LoginHandler response) {
                    handler.dealLogin(response);
                }

                @Override
                public void onCleanup(String key, LoginFragment handler, LoginHandler response) {
                    response.removeCompletionListener(handler.loginListener);
                }
            };
    private UpdateCompleteCallback<LoginHandler> loginListener = new UpdateCompleteCallback<LoginHandler>() {
        @Override
        public void onCompleted(LoginHandler response, boolean isSuccess) {
            dealLogin(response);
        }
    };

    private static StateUpdateHandlerListener<LoginFragment, QueryProductHandler> productsHandlerListener =
            new StateUpdateHandlerListener<LoginFragment, QueryProductHandler>() {
                @Override
                public void onUpdate(String key, LoginFragment handler, QueryProductHandler response) {
                    handler.dealQueryProductHandler(response);
                }

                @Override
                public void onCleanup(String key, LoginFragment handler, QueryProductHandler response) {
                    response.removeCompletionListener(handler.queryProductsListener);
                }
            };
    private UpdateCompleteCallback<QueryProductHandler> queryProductsListener =
            new UpdateCompleteCallback<QueryProductHandler>() {
                @Override
                public void onCompleted(QueryProductHandler response, boolean isSuccess) {
                    dealQueryProductHandler(response);
                }
            };

    LoginViewBinder viewBinder;

    public static LoginFragment newInstace() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        viewBinder = new LoginViewBinder();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewBinder.bindView(view);

        String lastUser = PreferencesUtils.getLastUser(getContext());
        if (!TextUtils.isEmpty(lastUser)) {
            viewBinder.accountEt.setText(lastUser);
            viewBinder.pwdEt.requestFocus();
        }
//        编辑完之后点击软键盘上的回车键才会触发
        viewBinder.pwdEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    toLogin();
                    return true;
                }
                return false;
            }
        });
        viewBinder.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLogin();
            }
        });
        viewBinder.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRegister();
            }
        });
        viewBinder.changePwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChangePassword();
            }
        });
    }

//    绑定在确认登录按钮触发事件
    private void toLogin() {
        String userNumber = viewBinder.accountEt.getText().toString();
        if (TextUtils.isEmpty(userNumber)) {
            viewBinder.accountEt.setError("用户编号不能为空");
            return;
        }
        String password = viewBinder.pwdEt.getText().toString();
        if (TextUtils.isEmpty(password)) {
            viewBinder.pwdEt.setError("登录密码不能为空");
            return;
        }
        updateForResponse("KEY_LOGIN", createLoginHandler(userNumber, password), handlerListener);
    }

    private LoginHandler createLoginHandler(String userNo, String password) {
        UserLogin userLogin = new UserLogin(userNo, password);
        userLogin.setMac(DeviceUtils.getWifiMac(getContext()));
        LoginHandler handler = new LoginHandler(userLogin, true);
        handler.run();
        return handler;
    }


    private void dealLogin(LoginHandler response) {
        if (response == null) {
            return;
        }
        synchronized (response.getStatusLock()) {
            if (response.isUpdating()) {
                showProgressDialog("登录中....");
                response.addCompletionListener(loginListener);
            } else {
                if (response.isSuccess() && response.getData().isSuccess()) {
                    loadProducts(); // 初始化,加载商品信息
                    saveLoginUser();
                } else {
                    showToast("登录失败:" + response.getData().getErrorMessage());
                    dismissProgressDialog();
                }
                cleanupResponse("KEY_LOGIN");
            }
        }
    }

    private void saveLoginUser() {
        PosInfo posInfo = PosDataManager.getInstance().getPosInfo();
        PreferencesUtils.saveLastUser(getContext(), posInfo.getUserNumber());
    }

    private void loadProducts() {
        QueryProductHandler handler = new QueryProductHandler(
                PosDataManager.getInstance(),
                PosDataManager.getInstance().getPosInfo());
        updateForResponse("KEY_LOAD_PRODUCTS", handler, productsHandlerListener);
        handler.run();
    }

    private void dealQueryProductHandler(QueryProductHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("正在初始化数据...");
                handler.addCompletionListener(queryProductsListener);
            } else {
                if (handler.isSuccess()) {
                    toHome();
                } else {
                    showToast("初始化数据失败");
                }
                dismissProgressDialog();
                cleanupResponse("KEY_LOAD_PRODUCTS");
            }
        }
    }

    private void toHome() {
        Intent intent = PlaceOrderActivity.getStartIntent(getActivity());
        Activity activity = getActivity();
        activity.startActivity(intent);
        activity.finish();
    }

//    绑定在注册按钮事件
    private void toRegister() {
        Intent intent = RegisterActivity.getStartIntent(getActivity());
        getActivity().startActivity(intent);
    }

//    绑定在修改密码按钮事件
    private void toChangePassword() {
        Intent intent = ChangePasswordActivity.getStartIntent(getActivity());
        getActivity().startActivity(intent);
    }
}
