package com.unioncloud.newpay.presentation.ui.coupon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.raizlabs.coreutils.threading.ThreadingUtils;
import com.unioncloud.newpay.NewPayApplication;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.utils.DateFormatUtils;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.coupon.CreateSaleCouponHandler;
import com.unioncloud.newpay.presentation.presenter.print.PrintCouponHandler;
import com.unioncloud.newpay.presentation.presenter.sharedpreferences.PreferencesUtils;

import java.util.Date;

/**
 * Created by cwj on 16/9/13.
 */
public class CreateSaleCouponFragment extends StatedFragment {

    public static CreateSaleCouponFragment newIntance() {
        CreateSaleCouponFragment fragment = new CreateSaleCouponFragment();
        return fragment;
    }

    private StateUpdateHandlerListener<CreateSaleCouponFragment, CreateSaleCouponHandler> createHandlerListener =
            new StateUpdateHandlerListener<CreateSaleCouponFragment, CreateSaleCouponHandler>() {
                @Override
                public void onUpdate(String key, CreateSaleCouponFragment handler, CreateSaleCouponHandler response) {
                    handler.dealCreateCoupon(response);
                }

                @Override
                public void onCleanup(String key, CreateSaleCouponFragment handler, CreateSaleCouponHandler response) {
                    response.removeCompletionListener(handler.createListener);
                }
            };
    private UpdateCompleteCallback<CreateSaleCouponHandler> createListener =
            new UpdateCompleteCallback<CreateSaleCouponHandler>() {
                @Override
                public void onCompleted(CreateSaleCouponHandler response, boolean isSuccess) {
                    dealCreateCoupon(response);
                }
            };

    private StateUpdateHandlerListener<CreateSaleCouponFragment, PrintCouponHandler> printHandlerListener =
            new StateUpdateHandlerListener<CreateSaleCouponFragment, PrintCouponHandler>() {
                @Override
                public void onUpdate(String key, CreateSaleCouponFragment handler, PrintCouponHandler response) {
                    handler.dealPrint(response);
                }

                @Override
                public void onCleanup(String key, CreateSaleCouponFragment handler, PrintCouponHandler response) {
                    response.removeCompletionListener(handler.printListener);
                }
            };
    private UpdateCompleteCallback<PrintCouponHandler> printListener =
            new UpdateCompleteCallback<PrintCouponHandler>() {
                @Override
                public void onCompleted(PrintCouponHandler response, boolean isSuccess) {
                    dealPrint(response);
                }
            };

    private EditText inputEt;
    private Button cancelBtn;
    private Button okBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_sale_coupon, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputEt = (EditText) view.findViewById(R.id.fragment_create_sale_coupon_input);
        cancelBtn = (Button) view.findViewById(R.id.fragment_create_sale_coupon_cancel_btn);
        okBtn = (Button) view.findViewById(R.id.fragment_create_sale_coupon_ok_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCoupon();
            }
        });
        String lastOrderId = getLastOrderId();
        if (!TextUtils.isEmpty(lastOrderId)) {
            inputEt.setText(getLastOrderId());
            inputEt.setSelection(lastOrderId.length());
        }
    }

    private String getLastOrderId() {
        PosInfo posInfo = PosDataManager.getInstance().getPosInfo();
        String lastSerialNumber = PreferencesUtils.getLastSerialNumber(NewPayApplication.getInstance());
        StringBuilder sb = new StringBuilder();
        sb.append(posInfo.getShopNumber())
                .append(DateFormatUtils.yyyyMMdd(new Date()))
                .append(posInfo.getPosNumber())
                .append(lastSerialNumber);
        return sb.toString();
    }

    private void createCoupon() {
        String orderId = inputEt.getText().toString();
        if (TextUtils.isEmpty(orderId)) {
            showToast("交易单号不能为空!");
            return;
        }
        CreateSaleCouponHandler handler = new CreateSaleCouponHandler(orderId,
                PosDataManager.getInstance().getPosInfo().getUserNumber());
        updateForResponse("CreateSaleCouponFragment:createSaleCoupon", handler, createHandlerListener);
        handler.run();
    }

    private void dealCreateCoupon(CreateSaleCouponHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("正在查询交易信息...");
                handler.addCompletionListener(createListener);
            } else {
                if (handler.isSuccess()) {
                    printCoupon(handler.getData());
                } else {
                    showToast("生成赠券失败");
                    dismissProgressDialog();
                }
                cleanupResponse("CreateSaleCouponFragment:createSaleCoupon");
            }
        }
    }

    private void printCoupon(Coupon coupon) {
        PrintCouponHandler handler = new PrintCouponHandler(getActivity(), coupon);
        updateForResponse("CreateSaleCouponFragment:printCoupon", handler, printHandlerListener);
        handler.run();
    }

    private void dealPrint(PrintCouponHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("正在打印折扣券...");
                handler.addCompletionListener(printListener);
            } else {
                dismissProgressDialog();
                cleanupResponse("CreateSaleCouponFragment:printCoupon");
                if (!handler.isSuccess() || !handler.getData()) {
                    showToast("打印失败");
                }
                onPrintFinish();
            }
        }
    }

    private void onPrintFinish() {
        ThreadingUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
