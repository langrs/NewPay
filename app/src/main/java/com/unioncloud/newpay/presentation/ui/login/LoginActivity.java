package com.unioncloud.newpay.presentation.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.esummer.android.activity.SingleFragmentActivity;
import com.esummer.android.fragment.FragmentStack;
import com.raizlabs.coreutils.app.FragmentStackManagerFragment;
import com.unioncloud.newpay.R;

/**
 * Created by cwj on 16/7/22.
 */
public class LoginActivity extends SingleFragmentActivity {
//    private FragmentStackManagerFragment stackManager;

    public static Intent getLoginIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected Fragment createContentFragment() {
        return LoginFragment.newInstace();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        stackManager = getStateManager().getFragmentStackManager(R.id.activity_single_frame);
        hideToolbar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        stackManager.saveInstanceState(outState);
    }

    @Override
    public boolean pop(FragmentStack stack, Fragment fragment) {
        return super.pop(stack, fragment);
    }

    @Override
    public boolean push(FragmentStack stack, Fragment fragment) {
        return super.push(stack, fragment);
    }

}
