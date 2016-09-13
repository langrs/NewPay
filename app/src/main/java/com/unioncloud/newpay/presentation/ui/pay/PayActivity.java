package com.unioncloud.newpay.presentation.ui.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.esummer.android.activity.BaseActivity;
import com.esummer.android.fragment.FragmentStack;
import com.esummer.android.fragment.StatedFragment;
import com.raizlabs.coreutils.app.FragmentStackManagerFragment;
import com.unioncloud.newpay.R;

/**
 * Created by cwj on 16/9/9.
 */
public class PayActivity extends BaseActivity implements OnPaidListener {

    public static Intent getStartIntent(Context context, PaymentSignpost signpost) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("paymentSignpost", signpost);
        return intent;
    }

    private FragmentStackManagerFragment stackManager;

    @Override
    public boolean push(FragmentStack stack, Fragment fragment) {
        if (stack == stackManager.getTop()) {
            stackManager.push(fragment);
            return true;
        }
        return false;
    }

    @Override
    public boolean pop(FragmentStack stack, Fragment fragment) {
        return (stack == stackManager.getTop()) && stackManager.pop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        stackManager = stateManager.getFragmentStackManager(R.id.activity_pay_frame);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (stackManager.isEmpty()) {
                    PaymentSignpost signpost = getPaymentSignPost();
                    if (signpost != null) {
                        stackManager.push(signpost.toPay());
                    }
                }
            }
        });

        hideActionBar();
    }

    private PaymentSignpost getPaymentSignPost() {
        return (PaymentSignpost) getIntent().getSerializableExtra("paymentSignpost");
    }

    @Override
    public void onBackPressed() {
//        if (!stackManager.pop()) {
            super.onBackPressed();
//        }
    }

    @Override
    public void onPaidSuccess() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void onPaidFail() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = stackManager.getTop();
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        stackManager = null;
        super.onDestroy();
    }
}
