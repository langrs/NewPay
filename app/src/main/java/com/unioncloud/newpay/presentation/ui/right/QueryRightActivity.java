package com.unioncloud.newpay.presentation.ui.right;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.esummer.android.activity.SingleFragmentActivity;

/**
 * Created by cwj on 16/9/13.
 */
public class QueryRightActivity extends SingleFragmentActivity implements RightListener {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, QueryRightActivity.class);
        return intent;
    }

    @Override
    protected Fragment createContentFragment() {
        return QueryRefundRightFragment.newInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onQueryFinish(boolean hasRight) {
        if (hasRight) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}
