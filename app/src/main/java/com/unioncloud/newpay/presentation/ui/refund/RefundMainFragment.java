package com.unioncloud.newpay.presentation.ui.refund;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.esummer.android.fragment.StatedFragment;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.order.OrderType;
import com.unioncloud.newpay.presentation.ui.placeorder.PlaceOrderActivity;

/**
 * Created by cwj on 16/8/22.
 */
public class RefundMainFragment extends StatedFragment {

    private View byOriginal;
    private View byNew;

    public static RefundMainFragment newInstance() {
        RefundMainFragment fragment = new RefundMainFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refund_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        byOriginal = view.findViewById(R.id.fragment_refund_from_original);
        byOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toQueryForRefund();
            }
        });

        byNew = view.findViewById(R.id.fragment_refund_from_new);
        byNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewRefundOrder();
            }
        });
    }

    private void toQueryForRefund() {
        Intent intent = RefundQueryActivity.getStartIntent(getActivity());
        startActivity(intent);
    }

    private void toNewRefundOrder() {
//        Intent intent = PlaceOrderActivity.getStartIntent(getActivity());
//        intent.putExtra("orderType", OrderType.REFUND);
//        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
