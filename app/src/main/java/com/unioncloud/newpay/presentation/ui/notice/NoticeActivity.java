package com.unioncloud.newpay.presentation.ui.notice;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.esummer.android.activity.BaseActivity;
import com.esummer.android.fragment.FragmentStack;
import com.raizlabs.coreutils.app.FragmentStackManagerFragment;
import com.unioncloud.newpay.R;

/**
 * Created by cwj on 16/9/12.
 */
public class NoticeActivity extends BaseActivity {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, NoticeActivity.class);
        return intent;
    }

    private FragmentStackManagerFragment stackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        setToolbar(com.esummer.android.R.id.toolbar);
        stackManager = stateManager.getFragmentStackManager(R.id.activity_notice_frame);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (stackManager.isEmpty()) {
                    NoticesFragment fragment = NoticesFragment.newInstance();
                    stackManager.push(fragment);
                    setTitle("消息列表");
                }
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean push(FragmentStack stack, Fragment fragment) {
        if (stack == stackManager.getTop()) {
            stackManager.push(fragment);
            setTitle("消息详情");
            return true;
        }
        return false;
    }

    @Override
    public boolean pop(FragmentStack stack, Fragment fragment) {
        boolean hasPop = false;
        if (stack == stackManager.getTop()) {
            hasPop = stackManager.pop();
            if (hasPop) {
                setTitle("消息列表");
            }
        }
        return hasPop;
    }

    @Override
    public void onBackPressed() {
        if (!stackManager.pop()) {
            super.onBackPressed();
        }
    }
}
