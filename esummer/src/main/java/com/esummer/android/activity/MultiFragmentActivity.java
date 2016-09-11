package com.esummer.android.activity;

import android.support.v4.app.Fragment;

import com.esummer.android.fragment.FragmentStack;
import com.raizlabs.coreutils.app.FragmentStackManagerFragment;

/**
 * Created by cwj on 16/7/19.
 */
public class MultiFragmentActivity extends BaseActivity {

    protected FragmentStackManagerFragment stackManager;




    @Override
    public boolean push(FragmentStack stack, Fragment fragment) {
        return false;
    }

    @Override
    public boolean pop(FragmentStack stack, Fragment fragment) {
        return false;
    }
}
