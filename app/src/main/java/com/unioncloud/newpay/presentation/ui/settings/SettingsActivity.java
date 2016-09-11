package com.unioncloud.newpay.presentation.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.esummer.android.activity.SingleFragmentActivity;

/**
 * Created by cwj on 16/9/3.
 */
public class SettingsActivity extends SingleFragmentActivity {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        return intent;
    }

    @Override
    protected Fragment createContentFragment() {
        return SettingsFragment.newInstance();
    }
}
