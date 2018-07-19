package com.unioncloud.newpay.presentation.ui.testp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esummer.android.fragment.StatedFragment;
import com.unioncloud.newpay.R;

public class TestpFragment extends StatedFragment {

    private TestpBinder testpBinder ;

    public TestpFragment() {
        // Required empty public constructor
    }
    public static TestpFragment newInstance() {
        TestpFragment fragment = new TestpFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_testp, container, false);
        testpBinder = new TestpBinder();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        testpBinder.btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
