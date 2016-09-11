package com.unioncloud.newpay.presentation.ui.login;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.esummer.android.widget.CleanableEditText;
import com.unioncloud.newpay.R;

/**
 * Created by cwj on 16/7/26.
 */
public class LoginViewBinder {
    public CleanableEditText accountEt;
    public CleanableEditText pwdEt;
    public Button loginBtn;
    public TextView registerBtn;
    public TextView changePwBtn;

    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    public void bindView(View view) {
        accountEt = (CleanableEditText) view.findViewById(R.id.login_account);
        pwdEt = (CleanableEditText) view.findViewById(R.id.login_password);
        loginBtn = (Button) view.findViewById(R.id.login_login);
        registerBtn = (TextView) view.findViewById(R.id.login_register);
        changePwBtn = (TextView) view.findViewById(R.id.login_changePassword);
    }

}
