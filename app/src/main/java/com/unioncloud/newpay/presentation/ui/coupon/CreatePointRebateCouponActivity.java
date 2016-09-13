package com.unioncloud.newpay.presentation.ui.coupon;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.esummer.android.activity.BaseActivity;
import com.esummer.android.dialog.DefaultDialogBuilder;
import com.esummer.android.dialog.UniversalDialog;
import com.esummer.android.fragment.FragmentStack;
import com.raizlabs.coreutils.app.FragmentStackManagerFragment;
import com.raizlabs.coreutils.functions.Delegate;
import com.unioncloud.newpay.R;

/**
 * Created by cwj on 16/9/13.
 */
public class CreatePointRebateCouponActivity extends BaseActivity {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CreatePointRebateCouponActivity.class);
        return intent;
    }

    private static final int CANCEL_COUPON_DIALOG_ID = UniversalDialog.getAutoId();

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
        setContentView(R.layout.activity_create_points_coupon);
        getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        stackManager = stateManager.getFragmentStackManager(R.id.activity_create_points_coupon_frame);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (stackManager.isEmpty()) {
                    QueryPointsRebateFragment fragment = QueryPointsRebateFragment.newInstance();
                    stackManager.push(fragment);
                }
            }
        });

        hideActionBar();
    }

    private DefaultDialogBuilder initCancelDialogBuilder() {
        DefaultDialogBuilder builder = new DefaultDialogBuilder(CANCEL_COUPON_DIALOG_ID);
        builder.setTitle("是否放弃打印券");
        builder.setMessage("积分返利券已生成,是否放弃打印!");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new Delegate<UniversalDialog>() {
            @Override
            public void execute(UniversalDialog dialog) {
                finish();
            }
        });
        builder.setNeutralButton("取消", null);
        return builder;
    }

    @Override
    public void onBackPressed() {
//        if (!stackManager.pop()) {
            super.onBackPressed();
//        }
    }
}
