package com.unioncloud.newpay.presentation.ui.coupon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esummer.android.fragment.StatedFragment;
import com.unioncloud.newpay.R;

/**
 * Created by cwj on 16/9/13.
 */
public class CreateCouponSelectorFragment extends StatedFragment {

    public static CreateCouponSelectorFragment newInstance() {
        CreateCouponSelectorFragment fragment = new CreateCouponSelectorFragment();
        return fragment;
    }

    private View saleCoupon;
    private View pointsCoupon;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_coupon_selector, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("兑券");
        saleCoupon = view.findViewById(R.id.fragment_create_coupon_by_sale);
        pointsCoupon = view.findViewById(R.id.fragment_create_coupon_by_points);

        saleCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSaleCoupon();
            }
        });
        pointsCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPointsCoupon();
            }
        });
    }

    private void toSaleCoupon() {
        CreateSaleCouponFragment fragment = CreateSaleCouponFragment.newIntance();
        fragment.show(getChildFragmentManager(), "CreateSaleCouponFragment");
    }

    private void toPointsCoupon() {
        Intent intent = CreatePointRebateCouponActivity.getStartIntent(getActivity());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
