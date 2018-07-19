package com.unioncloud.newpay.presentation.ui.testp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;

import com.esummer.android.activity.SingleFragmentActivity;
import com.esummer.android.fragment.FragmentStack;

public class TestpActivity extends SingleFragmentActivity {

//    获取本活动的intent
    public static Intent getTestpIntent(Context context){
        Intent intent = new Intent(context, TestpActivity.class);
        return intent;
    }

//    返回fragment
    @Override
    protected Fragment createContentFragment() {
        return TestpFragment.newInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideToolbar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean push(FragmentStack stack, Fragment fragment) {
       return super.push(stack,fragment);
    }

    @Override
    public boolean pop(FragmentStack stack, Fragment fragment) {
       return super.pop(stack,fragment);
    }
}