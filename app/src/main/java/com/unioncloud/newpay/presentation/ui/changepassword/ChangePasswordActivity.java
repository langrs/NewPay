package com.unioncloud.newpay.presentation.ui.changepassword;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.esummer.android.activity.SingleFragmentActivity;

/**
 * Created by cwj on 16/9/5.
 */
public class ChangePasswordActivity extends SingleFragmentActivity {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ChangePasswordActivity.class);
        return intent;
    }


    @Override
    protected Fragment createContentFragment() {
        return ChangePasswordFragment.newInstance();
    }

}
