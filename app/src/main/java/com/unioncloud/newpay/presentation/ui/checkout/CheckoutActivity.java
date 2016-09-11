package com.unioncloud.newpay.presentation.ui.checkout;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.esummer.android.activity.SingleFragmentActivity;

/**
 * Created by cwj on 16/8/4.
 */
public class CheckoutActivity extends SingleFragmentActivity {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CheckoutActivity.class);
        return intent;
    }

    @Override
    protected Fragment createContentFragment() {
        return CheckoutFragment.newInstance();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
//        CheckoutFragment fragment = (CheckoutFragment) findContentFragment();
//        if (fragment != null) {
//            fragment.onBackPressed();
//        }
        super.onBackPressed();
    }
}
