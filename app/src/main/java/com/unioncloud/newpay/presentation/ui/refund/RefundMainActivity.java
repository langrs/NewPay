package com.unioncloud.newpay.presentation.ui.refund;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.esummer.android.activity.SingleFragmentActivity;

/**
 * Created by cwj on 16/8/22.
 */
public class RefundMainActivity extends SingleFragmentActivity {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, RefundMainActivity.class);
        return intent;
    }

    @Override
    protected Fragment createContentFragment() {
        return RefundMainFragment.newInstance();
    }
}
