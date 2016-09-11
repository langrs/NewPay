package com.unioncloud.newpay.presentation.ui.refund;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.esummer.android.activity.BaseActivity;
import com.esummer.android.fragment.FragmentStack;
import com.raizlabs.coreutils.app.FragmentStackManagerFragment;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.presentation.model.refund.OriginalRefundInfo;
import com.unioncloud.newpay.presentation.ui.pay.PaymentSignpost;

/**
 * Created by cwj on 16/9/9.
 */
public class RefundActivity extends BaseActivity implements OnRefundListener {

    public static Intent getOriginalRefundIntent(Context context, PaymentSignpost signpost, OriginalRefundInfo info) {
        Intent intent = new Intent(context, RefundActivity.class);
        intent.putExtra("originalRefundInfo", info);
        intent.putExtra("paymentSignpost", signpost);
        return intent;
    }

    private FragmentStackManagerFragment stackManager;

    private PaymentSignpost getPaymentSignPost() {
        return (PaymentSignpost) getIntent().getSerializableExtra("paymentSignpost");
    }

    private OriginalRefundInfo getOriginalRefundInfo() {
        return getIntent().getParcelableExtra("originalRefundInfo");
    }

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
        setContentView(R.layout.activity_refund);
        getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        stackManager = stateManager.getFragmentStackManager(R.id.activity_refund_frame);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (stackManager.isEmpty()) {
                    PaymentSignpost signpost = getPaymentSignPost();
                    if (signpost != null) {
                        Fragment fragment = signpost.toRefund();
                        fragment.getArguments().putParcelable("originalRefundInfo", getOriginalRefundInfo());
                        stackManager.push(fragment);
                    }
                }
            }
        });
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
    public void onBackPressed() {
//        if (!stackManager.pop()) {
            super.onBackPressed();
//        }
    }

    @Override
    public void onRefundSuccess() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void onRefundFail() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
