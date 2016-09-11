package com.unioncloud.newpay.presentation.ui.register;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.esummer.android.activity.SingleFragmentActivity;

/**
 * Created by cwj on 16/7/26.
 */
public class RegisterActivity extends SingleFragmentActivity {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        return intent;
    }

    @Override
    protected Fragment createContentFragment() {
        return RegisterFragment.newInstance();
    }
}
