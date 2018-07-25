package com.esummer.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.esummer.android.R;
import com.esummer.android.fragment.FragmentStack;

/**
 * Created by cwj on 16/7/19.
 */
public abstract class SingleFragmentActivity extends BaseActivity {

    private static final String SINGLE_FRAGMENT_TAG = "SINGLE_FRAGMENT_ID";

    public Fragment findContentFragment() {
        return getSupportFragmentManager().findFragmentByTag(SINGLE_FRAGMENT_TAG);
    }

    protected boolean hasDrawer() {
        return false;
    }

    protected int getLayoutResId() {
//        是否有侧滑菜单布局
        return hasDrawer() ?
                R.layout.activity_single_frame_with_drawer :
                R.layout.activity_single_frame;
    }

    protected void hideToolbar() {
        Toolbar toolbar = findToolbar(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean push(FragmentStack stack, Fragment fragment) {
        return false;
    }

    @Override
    public boolean pop(FragmentStack stack, Fragment fragment) {
        finish();
        return true;
    }

    protected void restoreOnCreate(Bundle savedInstanceState) {

    }
    /*
    * 钩子函数,子类定义返回的fragment,这样在父类onCreate方法中就能被将该fragment在布局中调用到了
    * */
    protected abstract Fragment createContentFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        restoreOnCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        setToolbar(R.id.toolbar);
        if (findContentFragment() == null) {
            Fragment fragment = createContentFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_single_frame, fragment, SINGLE_FRAGMENT_TAG)
                    .commit();
        }
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Fragment contentFragment = findContentFragment();
//        if(contentFragment != null) {
//            contentFragment.onActivityResult(requestCode, resultCode, data);
//        } else {
            super.onActivityResult(requestCode, resultCode, data);
//        }
    }
}
