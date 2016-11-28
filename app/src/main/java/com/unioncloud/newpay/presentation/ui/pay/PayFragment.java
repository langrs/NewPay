package com.unioncloud.newpay.presentation.ui.pay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.esummer.android.fragment.StatedFragment;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;

import java.util.ArrayList;

/**
 * Created by cwj on 16/8/15.
 */
public abstract class PayFragment extends StatedFragment {

    private static ArrayList<String> keyboardList;

    static {
        keyboardList = new ArrayList<>(12);
        for (int i = 1; i < 13; i++) {
            if (i < 10) {
                keyboardList.add(String.valueOf(i));
            } else if (i == 10) {
                keyboardList.add(".");
            } else if (i == 11) {
                keyboardList.add(String.valueOf(0));
            } else if (i == 12) {
                keyboardList.add("<-");
            }
        }
    }

    protected abstract PaymentSignpost getPaymentSignpost();

    protected TextView titleTv;
    protected TextView unpayTv;
    protected TextView amountInputTv;
    protected GridView keyboard;
    protected Button okBtn;
    protected View fillInContainer;
    protected EditText fillInRelationNoEt;

    private OnPaidListener onPaidListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onPaidListener = (OnPaidListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleTv = (TextView) view.findViewById(R.id.fragment_pay_title);
        PaymentSignpost signpost = getPaymentSignpost();
        String title = signpost.getName();

        int titleColor = signpost.getTextColor();
        titleTv.setText(title);
        titleTv.setTextColor(titleColor);

        unpayTv = (TextView) view.findViewById(R.id.fragment_pay_uppay);
        amountInputTv = (TextView) view.findViewById(R.id.fragment_pay_willpay);
        keyboard = (GridView) view.findViewById(R.id.fragment_pay_input_keyboard);
        keyboard.setAdapter(new KeyboardAdapter(keyboardList));
        keyboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 11) {
                    deleteAmount();
                } else {
                    appendAmount(keyboardList.get(position));
                }
            }
        });

        okBtn = (Button) view.findViewById(R.id.fragment_pay_ok_button);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPay();
            }
        });

        String pay = MoneyUtils.fenToString(
                CheckoutDataManager.getInstance().getUnpayTotal());
        unpayTv.setText(pay);
        amountInputTv.setText(pay);
    }

    protected boolean isFillIn() {
        return getArguments().getBoolean("isFillIn");
    }

    protected void initFillInView(View rootView) {
        fillInContainer = rootView.findViewById(R.id.fragment_pay_fillin_container);
        fillInRelationNoEt = (EditText) rootView.findViewById(R.id.fragment_pay_fillin_relationNo);
        if (isFillIn()) {
            fillInContainer.setVisibility(View.VISIBLE);
        } else {
            fillInContainer.setVisibility(View.GONE);
        }
    }

    private void toPay() {
        String amount = getAmount();
        if (amount.endsWith(".")){
            amount = amount.replace(".", "");
        }
        int willPay = MoneyUtils.getFen(amount);
        if (willPay == 0) {
            showToast("无效的金额");
            return;
        }
        int unpay = CheckoutDataManager.getInstance().getUnpayTotal();
        pay(unpay, willPay);
    }

    protected abstract void pay(int unpay, int willPay);

    private String getAmount() {
        return amountInputTv.getText().toString();
    }

    private void appendAmount(String append) {
        String amount = getAmount();

        if(append.equals(".")) {
            if(!amount.contains(".")) {
                amount = amount + append;
            }
        } else if(amount.equals("0")) {
            amount = append;
        } else if (amount.equals("-0")) {
            amount = "-" + append;
//        } else if (!amount.matches("^\\-+\\d+(\\.\\d{2})$")){
        } else if (!amount.matches("^\\d+(\\.\\d{2})$")){
            amount = amount + append;
        }
        amountInputTv.setText(amount);
    }

    private void deleteAmount() {
        String amount = getAmount();
        int length = amount.length();
        if(length == 1) {
            amount = "0";
        } else if (length == 2 && amount.startsWith("-")){
            amount = "-0";
        } else {
            amount = amount.substring(0, length -1);
        }
        amountInputTv.setText(amount);
    }

    protected void onPaidSuccess() {
        if (onPaidListener != null) {
            onPaidListener.onPaidSuccess();
        }
    }

    protected void onPaidFail() {
        if (onPaidListener != null) {
            onPaidListener.onPaidFail();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        titleTv = null;
        unpayTv = null;
        amountInputTv= null;
        keyboard = null;
        okBtn = null;
        onPaidListener = null;
    }
}
