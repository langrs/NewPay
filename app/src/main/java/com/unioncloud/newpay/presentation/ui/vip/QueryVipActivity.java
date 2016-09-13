package com.unioncloud.newpay.presentation.ui.vip;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.esummer.android.activity.BaseActivity;
import com.esummer.android.fragment.FragmentStack;
import com.raizlabs.coreutils.app.FragmentStackManagerFragment;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.erp.VipCard;

/**
 * Created by cwj on 16/9/13.
 */
public class QueryVipActivity extends BaseActivity implements QueryVipCallback {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, QueryVipActivity.class);
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
        setContentView(R.layout.activity_query_vip);
        getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        stackManager = stateManager.getFragmentStackManager(R.id.activity_query_vip_frame);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (stackManager.isEmpty()) {
                    QueryVipFragment fragment = QueryVipFragment.newInstance();
                    stackManager.push(fragment);
                }
            }
        });
        hideActionBar();
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

    @Override
    public void onBackPressed() {
        if (!stackManager.pop()) {
            super.onBackPressed();
            onQueryCancel();
        }
    }

    @Override
    public void onQuerySuccess(VipCard vipCard) {
        Intent intent = new Intent();
        intent.putExtra("vip", vipCard);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onQueryCancel() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
