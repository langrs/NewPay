package com.unioncloud.newpay.presentation.ui.coupon;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.esummer.android.activity.SingleFragmentActivity;

/**
 * Created by cwj on 16/9/13.
 */
public class CreateCouponSelectorActivity extends SingleFragmentActivity {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CreateCouponSelectorActivity.class);
        return intent;
    }

    @Override
    protected Fragment createContentFragment() {
        return CreateCouponSelectorFragment.newInstance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
