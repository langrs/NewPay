package com.unioncloud.newpay.presentation.ui.refund;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.esummer.android.fragment.StatedFragment;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.presentation.model.refund.OriginalRefundInfo;
import com.unioncloud.newpay.presentation.ui.pay.PaymentSignpost;

/**
 * Created by cwj on 16/8/23.
 */
public abstract class RefundFragment extends StatedFragment {

    protected TextView titleTv;
    protected TextView refundTotalTv;       // 该支付方式退款总额
//    protected EditText willRefundAmountEt;  // 当前退款
    protected View originalContainerView;   // 原凭证号的录入View
    protected TextView originalIdLabel;
    protected EditText originalIdEt;        // 原交易凭证
    protected Button okBtn;

    protected abstract PaymentSignpost getPaymentSignpost();

    private OnRefundListener onRefundListener;


    public void setOriginalRefundInfo(OriginalRefundInfo info) {
        getArguments().putParcelable("originalRefundInfo", info);
    }

    public OriginalRefundInfo getOriginalRefundInfo() {
        return getArguments().getParcelable("originalRefundInfo");
    }

    public String getPaymentName() {
        PaymentSignpost signpost = getPaymentSignpost();
        if (signpost == null) {
            return "退款";
        }
        return getPaymentSignpost().getName();
    }

    private int getTitleColor() {
        PaymentSignpost signpost = getPaymentSignpost();
        if (signpost == null) {
            return R.color.TextColor_Normal;
        }
        return getPaymentSignpost().getTextColor();
    }

    public void setRefundAmount(int amount) {
        getArguments().putInt("refundAmount", amount);
    }

    public int getRefundAmount() {
        OriginalRefundInfo info = getOriginalRefundInfo();
        if (info != null) {
            return info.getRefundAmount();
        }
        return 0;
    }

    public String getRefundSaleDate(){
        OriginalRefundInfo info = getOriginalRefundInfo();
        if(info != null){
            return info.getOriginalSaleDate();
        }
        return "";
    }

    public void setOriginalOrderId(String orderId) {
        getArguments().putString("originalOrderId", orderId);
    }

    public String getOriginalOrderId() {
        OriginalRefundInfo info = getOriginalRefundInfo();
        if (info != null) {
            return info.getOriginalOrderId();
        }
        return null;
    }

    public void setOriginalBillNo(String billNo) {
        getArguments().putString("originalBillNo", billNo);
    }

    public String getOriginalBillNo() {
        OriginalRefundInfo info = getOriginalRefundInfo();
        if (info != null) {
            return info.getOriginalBillNo();
        }
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRefundListener = (OnRefundListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pay_refund, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleTv = (TextView) view.findViewById(R.id.fragment_pay_refund_title);
        String title = getPaymentName();
        if (title != null && !title.endsWith("退款")) {
            title = title + "退款";
        }
        int titleColor = getTitleColor();
        titleTv.setText(title);
        titleTv.setTextColor(titleColor);

        refundTotalTv = (TextView) view.findViewById(R.id.fragment_pay_refund_amount);
        refundTotalTv.setText(MoneyUtils.fenToString(getRefundAmount()));

        originalContainerView = view.findViewById(R.id.fragment_pay_original_container);
        originalIdLabel = (TextView) view.findViewById(R.id.fragment_pay_original_label);
        originalIdEt = (EditText) view.findViewById(R.id.fragment_pay_original);
        okBtn = (Button) view.findViewById(R.id.fragment_pay_refund_ok_button);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refund();
            }
        });
    }

    protected abstract void refund();

    protected void onRefundSuccess() {
//        if (getTargetFragment() != null) {
//            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
//        } else {
//            getParentFragment().onActivityResult(0, Activity.RESULT_OK, null);
//        }
//        dismiss();
        if (onRefundListener != null) {
            onRefundListener.onRefundSuccess();
        }
    }

    protected void onRefundFailed() {
//        if (getTargetFragment() != null) {
//            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
//        } else {
//            getParentFragment().onActivityResult(0, Activity.RESULT_CANCELED, null);
//        }
//        dismiss();
        if (onRefundListener != null) {
            onRefundListener.onRefundFail();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onRefundListener = null;
    }
}
