package com.unioncloud.newpay.presentation.ui.coupon;

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

import com.esummer.android.FragmentStackDelegate;
import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.coupon.QueryWeChatCouponHandler;
import com.unioncloud.newpay.presentation.ui.pay.CouponFragment;
import com.zbar.scan.ScanCaptureActivity;

/**
 * Created by cwj on 16/9/6.
 */
public class QueryWeChatCouponFragment extends StatedFragment {

    public static QueryWeChatCouponFragment newInstance() {
        QueryWeChatCouponFragment fragment = new QueryWeChatCouponFragment();
        return fragment;
    }

    private static StateUpdateHandlerListener<QueryWeChatCouponFragment, QueryWeChatCouponHandler> queryHandlerListener
            = new StateUpdateHandlerListener<QueryWeChatCouponFragment, QueryWeChatCouponHandler>() {
        @Override
        public void onUpdate(String key, QueryWeChatCouponFragment handler, QueryWeChatCouponHandler response) {
            handler.dealQueryCoupon(response);
        }

        @Override
        public void onCleanup(String key, QueryWeChatCouponFragment handler, QueryWeChatCouponHandler response) {
            response.removeCompletionListener(handler.queryListener);
        }
    };
    private UpdateCompleteCallback<QueryWeChatCouponHandler> queryListener =
            new UpdateCompleteCallback<QueryWeChatCouponHandler>() {
                @Override
                public void onCompleted(QueryWeChatCouponHandler response, boolean isSuccess) {
                    dealQueryCoupon(response);
                }
            };

    private static final int REQUEST_SCAN_CODE = 1000;

    protected TextView titleTv;
    protected View inputContainerView;
    protected EditText inputEt;
    protected Button cancelButton;
    protected Button okButton;
    protected TextView tipsTv;
    protected TextView scanTv;

    private FragmentStackDelegate stackDelegate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        stackDelegate = (FragmentStackDelegate) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_query_coupon, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleTv = (TextView) view.findViewById(R.id.fragment_query_title);
        inputContainerView = view.findViewById(R.id.fragment_query_input_container);
        inputEt = (EditText) view.findViewById(R.id.fragment_query_input);
        tipsTv = (TextView) view.findViewById(R.id.fragment_query_message);
        cancelButton = (Button) view.findViewById(R.id.fragment_query_cancel_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dismiss();
                getActivity().finish();
            }
        });
        okButton = (Button) view.findViewById(R.id.fragment_query_ok_btn);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryByInput();
            }
        });
        scanTv = (TextView) view.findViewById(R.id.fragment_query_scan);
        scanTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toStartScan();
            }
        });
    }

    private void toStartScan() {
        Intent intent = ScanCaptureActivity.getStartIntent(getActivity(), "请扫描二维码会员卡");
        startActivityForResult(intent, REQUEST_SCAN_CODE);
    }

    private void queryByInput() {
        String input = inputEt.getText().toString();
        if (TextUtils.isEmpty(input)) {
            showToast("请输入有效号码");
            return;
        }
        queryCoupon(input);
    }

//根据券号查询微信卡券信息
    private void queryCoupon(String couponNo) {
        String shopId = PosDataManager.getInstance().getPosInfo().getShopId();
        QueryWeChatCouponHandler handler = new QueryWeChatCouponHandler(
                couponNo,shopId);
        updateForResponse("QueryCouponFragment:queryCoupon", handler, queryHandlerListener);
        handler.run();
    }

    private void dealQueryCoupon(QueryWeChatCouponHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("正在查询...");
                handler.addCompletionListener(queryListener);
            } else {
                if (handler.isSuccess()) {
                    toCouponPay(handler.getData());
                } else {
                    showToast("查询失败,请确认券是否有效");
                }
                dismissProgressDialog();
                cleanupResponse("QueryCouponFragment:queryCoupon");
            }
        }
    }

    private void toCouponPay(Coupon coupon) {
        String flag = coupon.getFlag();
        CouponFragment fragment = null;
        if ("2".equals(flag)) {
            fragment = CouponFragment.newDiscountCoupon(coupon);
        } else if ("1".equals(flag)) {
            fragment = CouponFragment.newPointReturnCoupon(coupon);
        } else if("3".equals(flag)){
            fragment = CouponFragment.newWeChatCoupon(coupon);
        }else {
            showToast("无效的券类型！");
            return;
        }
        if (fragment != null && stackDelegate != null) {
            stackDelegate.push(this, fragment);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SCAN_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String code = data.getStringExtra("SCAN_RESULT");
//                if (isVisible()) {
//                    queryCoupon(code);
//                } else {
                    getArguments().putString("scan_code", code);
//                }
                return;
            }
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
                queryCoupon(scan_code);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stackDelegate = null;
    }

}
