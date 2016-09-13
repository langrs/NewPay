package com.unioncloud.newpay.presentation.ui.placeorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.esummer.android.activity.SingleFragmentActivity;
import com.esummer.android.dialog.DefaultDialogBuilder;
import com.esummer.android.dialog.UniversalDialog;
import com.raizlabs.coreutils.functions.Delegate;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.order.OrderType;
import com.unioncloud.newpay.presentation.model.cart.CartDataManager;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.ui.coupon.CreateCouponSelectorActivity;
import com.unioncloud.newpay.presentation.ui.notice.NoticeActivity;
import com.unioncloud.newpay.presentation.ui.order.OrderHistoryActivity;
import com.unioncloud.newpay.presentation.ui.order.OrderTodayCountActivity;
import com.unioncloud.newpay.presentation.ui.refund.RefundMainActivity;
import com.unioncloud.newpay.presentation.ui.settings.SettingsActivity;

/**
 * Created by cwj on 16/8/9.
 */
public class PlaceOrderActivity extends SingleFragmentActivity {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, PlaceOrderActivity.class);
        return intent;
    }

    private static final int EMPTY_CART_DIALOG_ID = UniversalDialog.getAutoId();

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
//    private ImageButton checkoutButton;

    private DefaultDialogBuilder emptyCartDialog;
    private int selectedNavigationId = 0;

    @Override
    protected Fragment createContentFragment() {
        OrderType orderType = (OrderType) getIntent().getSerializableExtra("orderType");
        if (orderType != null) {
            return PlaceOrderFragment.getInstance(orderType);
        }
        return PlaceOrderFragment.getInstance();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_place_order;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar = findToolbar(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_single_frame_drawer);
        navigationView = (NavigationView) findViewById(R.id.activity_single_frame_drawer_content);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0) {
            @Override
            public void onDrawerOpened(View drawerView) {
                navigationView.setCheckedItem(R.id.navigation_browse);
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerToggle.syncState();//初始化状态
        drawerLayout.setDrawerListener(mDrawerToggle);

        emptyCartDialog = initEmptyDialogBuilder();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                int itemId = item.getItemId();
                switch (itemId){
                    case R.id.navigation_search:
                    case R.id.navigation_refund:
                    case R.id.navigation_count:
                    case R.id.navigation_setting:
                    case R.id.navigation_notices:
                    case R.id.navigation_create_coupon:
                        selectedNavigationId = itemId;
                        emptyCartOrNavigation();
                        break;
                }
                return true;
            }
        });
    }

    private DefaultDialogBuilder initEmptyDialogBuilder() {
        DefaultDialogBuilder builder = new DefaultDialogBuilder(EMPTY_CART_DIALOG_ID);
        builder.setTitle("清空购物车");
        builder.setMessage("跳转页面将清空购物车!");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new Delegate<UniversalDialog>() {
            @Override
            public void execute(UniversalDialog dialog) {
                onEmptyCartOk();
            }
        });
        builder.setNeutralButton("取消", null);
        return builder;
    }

    private void emptyCartOrNavigation() {
        if (!CartDataManager.getInstance().isEmpty()) {
            getStateManager().showDialog(emptyCartDialog);
        } else {
            toNavigation();
        }
    }

    private void onEmptyCartOk() {
        CheckoutDataManager.getInstance().clear();
        toNavigation();
    }

    private void toNavigation() {
        switch (selectedNavigationId) {
            case R.id.navigation_search:
                selectedNavigationId = 0;
                toRefund();
                return;
            case R.id.navigation_refund:
                selectedNavigationId = 0;
                toSearch();
                return;
            case R.id.navigation_setting:
                selectedNavigationId = 0;
                toSettings();
                return;
            case R.id.navigation_count:
                selectedNavigationId = 0;
                toTodayCount();
                return;
            case R.id.navigation_notices:
                selectedNavigationId = 0;
                toNotice();
                return;
            case R.id.navigation_create_coupon:
                selectedNavigationId = 0;
                toCreateCoupon();
                return;
        }
    }

    private void toRefund() {
        Intent intent = RefundMainActivity.getStartIntent(this);
        startActivity(intent);
    }

    private void toSearch() {
        Intent intent = OrderHistoryActivity.getStartIntent(this);
        startActivity(intent);
    }

    private void toSettings() {
        Intent intent = SettingsActivity.getStartIntent(this);
        startActivity(intent);
    }

    private void toTodayCount() {
        Intent intent = OrderTodayCountActivity.getStartIntent(this);
        startActivity(intent);
    }

    private void toNotice() {
        Intent intent = NoticeActivity.getStartIntent(this);
        startActivity(intent);
    }

    private void toCreateCoupon() {
        Intent intent = CreateCouponSelectorActivity.getStartIntent(this);
        startActivity(intent);
    }

    @Override
    protected boolean hasDrawer() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onBackPressed() {
        PlaceOrderFragment  fragment = (PlaceOrderFragment) findContentFragment();
        if (fragment != null) {
            fragment.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        getStateManager().clearActionListener(emptyCartDialog);
        emptyCartDialog = null;
        super.onDestroy();
    }

    @Override
    public FragmentManager providerFM() {
        return getSupportFragmentManager();
    }
}
