package com.unioncloud.newpay.presentation.ui.order;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.esummer.android.activity.SingleFragmentActivity;
import com.unioncloud.newpay.domain.model.order.SaleOrder;

/**
 * Created by cwj on 16/8/25.
 */
public class OrderDetailActivity extends SingleFragmentActivity {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        return intent;
    }

    @Override
    protected Fragment createContentFragment() {
        SaleOrder order = (SaleOrder) getIntent().getSerializableExtra("order");

        return OrderDetailFragment.newIntance(order);
    }
}
