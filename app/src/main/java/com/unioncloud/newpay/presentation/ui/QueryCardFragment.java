package com.unioncloud.newpay.presentation.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.esummer.android.fragment.StatedFragment;
import com.pax.baselib.card.TrackData;
import com.raizlabs.coreutils.functions.Delegate;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.presentation.presenter.vip.SwipeCardManager;

/**
 * Created by cwj on 16/9/3.
 */
public abstract class QueryCardFragment extends StatedFragment {

    protected TextView titleTv;
    protected View inputContainerView;
    protected EditText inputEt;
    protected Button cancelButton;
    protected Button okButton;
    protected TextView tipsTv;

    private Delegate<TrackData> swipeListener = new Delegate<TrackData>() {
        @Override
        public void execute(TrackData trackData) {
            queryCardByTrack(trackData);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_query_card, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleTv = (TextView) view.findViewById(R.id.fragment_query_card_title);
        inputContainerView = view.findViewById(R.id.fragment_query_card_input_container);
        inputEt = (EditText) view.findViewById(R.id.fragment_query_card_input);
        tipsTv = (TextView) view.findViewById(R.id.fragment_query_card_message);
        cancelButton = (Button) view.findViewById(R.id.fragment_query_card_cancel_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelQuery();
            }
        });
        okButton = (Button) view.findViewById(R.id.fragment_query_card_ok_btn);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryCardByInput();
            }
        });

        String title = getTitle();
        if (TextUtils.isEmpty(title)) {
            title = "卡查询";
        }
        titleTv.setText(title);
        if (isStrictMode()) {
            inputContainerView.setVisibility(View.GONE);
            okButton.setVisibility(View.GONE);
            tipsTv.setVisibility(View.VISIBLE);
        } else {
            inputContainerView.setVisibility(View.VISIBLE);
            okButton.setVisibility(View.VISIBLE);
            tipsTv.setVisibility(View.GONE);
        }
    }

    private void queryCardByInput() {
        String inputStr = inputEt.getText().toString();
        if (TextUtils.isEmpty(inputStr)) {
            showToast("输入的关联号码不能为空!");
            return;
        }
        queryCardByBillNo(inputStr);
    }

    protected abstract void onCancelQuery();

    protected boolean isStrictMode() {
        return false;
    }

    protected abstract String getTitle();

    protected abstract void queryCardByBillNo(String billNo);

    protected abstract void queryCardByTrack(TrackData trackData);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SwipeCardManager.getInstance().addSwipeListener(swipeListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SwipeCardManager.getInstance().removeSwipeListener(swipeListener);
    }
}
