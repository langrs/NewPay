package com.unioncloud.newpay.presentation.ui.vip;

import android.app.Activity;
import android.content.Intent;
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
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.pax.baselib.card.TrackData;
import com.raizlabs.coreutils.functions.Delegate;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.vip.QueryVipHandler;
import com.unioncloud.newpay.presentation.presenter.vip.SwipeCardManager;
import com.zbar.scan.ScanCaptureActivity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by cwj on 16/8/14.
 */
public class AddVipFragment extends StatedFragment {

    public static AddVipFragment newInstance() {
        AddVipFragment fragment = new AddVipFragment();

        return fragment;
    }

    private static StateUpdateHandlerListener<AddVipFragment, QueryVipHandler> handlerListener =
            new StateUpdateHandlerListener<AddVipFragment, QueryVipHandler>() {
                @Override
                public void onUpdate(String key, AddVipFragment handler, QueryVipHandler response) {
                    handler.dealQueryVip(response);
                }

                @Override
                public void onCleanup(String key, AddVipFragment handler, QueryVipHandler response) {
                    response.removeCompletionListener(handler.queryVipListener);
                }
            };
    private UpdateCompleteCallback<QueryVipHandler> queryVipListener = new UpdateCompleteCallback<QueryVipHandler>() {
        @Override
        public void onCompleted(QueryVipHandler response, boolean isSuccess) {
            dealQueryVip(response);
        }
    };


    private Delegate<TrackData> swipeListener = new Delegate<TrackData>() {
        @Override
        public void execute(TrackData trackData) {
            queryVipByTrack(trackData);
        }
    };

    private EditText inputEt;
    private Button cancelButton;
    private Button okButton;
    private TextView scanBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_query_card, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputEt = (EditText) view.findViewById(R.id.fragment_query_card_input);
        cancelButton = (Button) view.findViewById(R.id.fragment_query_card_cancel_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        okButton = (Button) view.findViewById(R.id.fragment_query_card_ok_btn);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryVipByInput();
            }
        });
        scanBtn = (TextView) view.findViewById(R.id.fragment_query_card_scan);
        scanBtn.setVisibility(View.VISIBLE);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toStartScan();
            }
        });
    }

    // 是否正在查询会员卡
    private boolean isVipQuerying() {
        String[] loadingKeys = getArguments().getStringArray("CartFragment:loadingItemsArray");
        return ((loadingKeys != null) && loadingKeys.length != 0);
    }

    /** 将正在加载的过程状态移除 */
    private void removeLoadingItem(String loadingKey) {
        String[] loadingKeys = getArguments().getStringArray("CartFragment:loadingItemsArray");
        if (loadingKeys != null) {
            ArrayList<String> loadingKeyList = new ArrayList<>(Arrays.asList(loadingKeys));
            loadingKeyList.remove(loadingKey);
            getArguments().putStringArray("CartFragment:loadingItemsArray",
                    loadingKeyList.toArray(new String[loadingKeyList.size()]));
        }
    }

    /** 将正在加载的过程状态移除保存 */
    private void putLoadingItem(String loadingKey) {
        String[] loadingKeys = getArguments().getStringArray("CartFragment:loadingItemsArray");
        if (loadingKeys == null) {
            loadingKeys = new String[0];
        }
        ArrayList<String> loadingKeyList = new ArrayList<>(Arrays.asList(loadingKeys));
        if (!loadingKeyList.contains(loadingKey)) {
            loadingKeyList.add(loadingKey);
        }
        getArguments().putStringArray("CartFragment:loadingItemsArray",
                loadingKeyList.toArray(new String[loadingKeyList.size()]));
    }

    private void queryVipByTrack(TrackData trackData) {
        if (!isVipQuerying()) {
            putLoadingItem("AddVipFragment:VipQuerying");
            QueryVipHandler handler = new QueryVipHandler(trackData.getTrack2(), null,
                    PosDataManager.getInstance().getPosInfo());
            updateForResponse("AddVipFragment:VipQuerying", handler, handlerListener);
            handler.run();
        }
    }

    private void queryVipByInput() {
        if (!isVipQuerying()) {
            String billNo = inputEt.getText().toString();
            if (TextUtils.isEmpty(billNo)) {
                showToast("查询号码不能为空");
                return;
            }
            queryVipByBillNo(billNo);
        }
    }

    private void queryVipByBillNo(String billNo) {
        if (!isVipQuerying()) {
            putLoadingItem("AddVipFragment:VipQuerying");
            QueryVipHandler handler = new QueryVipHandler(null, billNo,
                    PosDataManager.getInstance().getPosInfo());
            updateForResponse("AddVipFragment:VipQuerying", handler, handlerListener);
            handler.run();
        }
    }

    private void dealQueryVip(QueryVipHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog();
                handler.addCompletionListener(queryVipListener);
            } else {
                removeLoadingItem("AddVipFragment:VipQuerying");
                cleanupResponse("AddVipFragment:VipQuerying");
                dismissProgressDialog();
                if (handler.isSuccess()) {
                    dismiss();
                } else {
                    showToast("查询会员卡失败");
                }
            }
        }
    }

    private void toStartScan() {
        Intent intent = ScanCaptureActivity.getStartIntent(getActivity(), "请扫描二维码会员卡");
        startActivityForResult(intent, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                String code = data.getStringExtra("SCAN_RESULT");
                getArguments().putString("scan_code", code);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getArguments().containsKey("scan_code")) {
            String scan_code = getArguments().getString("scan_code");
            getArguments().remove("scan_code");
            if (!TextUtils.isEmpty(scan_code)) {
                queryVipByBillNo(scan_code);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        SwipeCardManager.getInstance().addSwipeListener(swipeListener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SwipeCardManager.getInstance().removeSwipeListener(swipeListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
