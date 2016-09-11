package com.unioncloud.newpay.presentation.ui.order;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.esummer.android.activity.SingleFragmentActivity;

/**
 * Created by cwj on 16/8/30.
 */
public class OrderHistoryActivity extends SingleFragmentActivity {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, OrderHistoryActivity.class);
        return intent;
    }

    @Override
    protected Fragment createContentFragment() {
        return OrderHistoryFragment.newInstance();
    }
}
