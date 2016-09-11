package com.unioncloud.newpay.presentation.ui.refund;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.print.PrintThirdPartyOrder;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyRefundOrder;
import com.unioncloud.newpay.domain.utils.DateFormatUtils;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.model.refund.RefundDataManager;
import com.unioncloud.newpay.presentation.presenter.payment.ThirdPartyRefundHandler;
import com.unioncloud.newpay.presentation.presenter.print.PrintThirdPartyHandler;
import com.unioncloud.newpay.presentation.ui.pay.PaymentSignpost;
import com.zbar.scan.ScanCaptureActivity;

import java.util.Date;

/**
 * Created by cwj on 16/9/1.
 */
public class ThirdPartyRefundFragment extends RefundFragment {

    public static ThirdPartyRefundFragment newAliPay() {
        ThirdPartyRefundFragment fragment = new ThirdPartyRefundFragment();
        fragment.getArguments().putInt(KEY_ARGS_THIRDPARTY_REFUND_TYPE, REFUND_TYPE_ALI);
        return fragment;
    }

    public static ThirdPartyRefundFragment newWechatPay() {
        ThirdPartyRefundFragment fragment = new ThirdPartyRefundFragment();
        fragment.getArguments().putInt(KEY_ARGS_THIRDPARTY_REFUND_TYPE, REFUND_TYPE_WECHAT);
        return fragment;
    }

    private static final String KEY_ARGS_THIRDPARTY_REFUND_TYPE = "third_party_refund_type";
    private static final int REFUND_TYPE_ALI = 0x0010;
    private static final int REFUND_TYPE_WECHAT =  0x0020;
    private static final int REQUEST_SCAN_ORIGINAL_ID = 1000;

    private static StateUpdateHandlerListener<ThirdPartyRefundFragment, ThirdPartyRefundHandler> refundHandlerListener =
            new StateUpdateHandlerListener<ThirdPartyRefundFragment, ThirdPartyRefundHandler>() {
                @Override
                public void onUpdate(String key, ThirdPartyRefundFragment handler, ThirdPartyRefundHandler response) {
                    handler.dealRefund(response);
                }

                @Override
                public void onCleanup(String key, ThirdPartyRefundFragment handler, ThirdPartyRefundHandler response) {
                    response.removeCompletionListener(handler.refundListener);
                }
            };
    private UpdateCompleteCallback<ThirdPartyRefundHandler> refundListener =
            new UpdateCompleteCallback<ThirdPartyRefundHandler>() {
                @Override
                public void onCompleted(ThirdPartyRefundHandler response, boolean isSuccess) {
                    dealRefund(response);
                }
            };
    private static StateUpdateHandlerListener<ThirdPartyRefundFragment, PrintThirdPartyHandler> printHandlerListener =
            new StateUpdateHandlerListener<ThirdPartyRefundFragment, PrintThirdPartyHandler>() {
                @Override
                public void onUpdate(String key, ThirdPartyRefundFragment handler, PrintThirdPartyHandler response) {
                    handler.dealPrint(response);
                }

                @Override
                public void onCleanup(String key, ThirdPartyRefundFragment handler, PrintThirdPartyHandler response) {
                    response.removeCompletionListener(handler.printListener);
                }
            };
    private UpdateCompleteCallback<PrintThirdPartyHandler> printListener = new UpdateCompleteCallback<PrintThirdPartyHandler>() {
        @Override
        public void onCompleted(PrintThirdPartyHandler response, boolean isSuccess) {
            dealPrint(response);
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        originalContainerView.setVisibility(View.GONE);
    }

    @Override
    protected PaymentSignpost getPaymentSignpost() {
        int type = getArguments().getInt(KEY_ARGS_THIRDPARTY_REFUND_TYPE);
        switch (type) {
            case REFUND_TYPE_ALI:
                return PaymentSignpost.ALI;
            case REFUND_TYPE_WECHAT:
                return PaymentSignpost.WECHAT;
            default:
                return null;
        }
    }

    @Override
    protected void refund() {
        toScanCode();
    }

    private void toScanCode() {
        Intent intent = ScanCaptureActivity.getStartIntent(getActivity(), "扫描用户手机上的原订单号");
        startActivityForResult(intent, REQUEST_SCAN_ORIGINAL_ID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SCAN_ORIGINAL_ID) {
            if (resultCode == Activity.RESULT_OK) {
                String code = data.getStringExtra("SCAN_RESULT");
//                thirdPartyRefund(code);
                getArguments().putString("scan_code", code);
            } else {
                showToast("已取消扫描");
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments().containsKey("scan_code")) {
            String code = getArguments().getString("scan_code");
            getArguments().remove("scan_code");
            if (!TextUtils.isEmpty(code)) {
                thirdPartyRefund(code);
            }
        }
    }

    private void thirdPartyRefund(String code) {
        if (code == null || !code.equals(getOriginalBillNo())) {
            showToast("单号有误,请确认单号是否正确!");
            return;
        }
        ThirdPartyRefundOrder order = new ThirdPartyRefundOrder();
        order.setRefundOrderId(RefundDataManager.getInstance().createOrderId());
        order.setRefundFee(getRefundAmount());
        order.setThirdOrderId(code);
        ThirdPartyRefundHandler handler = new ThirdPartyRefundHandler(order, getRefundAmount(),
                getPaymentSignpost().numberToInt());
        updateForResponse("ThirdPartyRefundFragment:refund", handler, refundHandlerListener);
        handler.run();
    }

    private void dealRefund(ThirdPartyRefundHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog();
                handler.addCompletionListener(refundListener);
            } else {
                if (handler.isSuccess() && handler.getData().isSuccess()) {
                    showToast(getPaymentName() + "退款成功");
                    print(handler.getData().getData());
                } else {
                    showToast(getPaymentName() + "退款失败");
                    dismissProgressDialog();
                    onRefundFailed();
                }
                cleanupResponse("ThirdPartyRefundFragment:refund");
            }
        }
    }

    private void print(ThirdPartyRefundOrder order) {
        PrintThirdPartyOrder printInfo = new PrintThirdPartyOrder();
        printInfo.setOrderTitle(getPaymentSignpost().getName() + "签购单");
        PosInfo posInfo = PosDataManager.getInstance().getPosInfo();
        printInfo.setShopName("合胜百货");
        printInfo.setStoreName(posInfo.getShopName());
        printInfo.setPosNo(posInfo.getPosNumber());
        printInfo.setUserNo(posInfo.getUserNumber());
        printInfo.setThirdPartyOrderId(order.getThirdOrderId());
        if (!TextUtils.isEmpty(order.getRefundTime())) {
            printInfo.setDate(order.getRefundTime());
        } else {
            printInfo.setDate(DateFormatUtils.yyyyMMddHHmmss(new Date()));
        }
        printInfo.setAmount(MoneyUtils.fenToString(-order.getRefundFee()));
        printInfo.setMchId("7551000001"); // 第三方支付的商户号
        printInfo.setTradeType("退款");

        PrintThirdPartyHandler handler = new PrintThirdPartyHandler(getActivity(), printInfo);
        updateForResponse("ThirdPartyPayFragment:printPay", handler, printHandlerListener);
        handler.print();
    }

    private void dealPrint(PrintThirdPartyHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog();
                handler.addCompletionListener(printListener);
            } else {
                dismissProgressDialog();
                cleanupResponse("ThirdPartyPayFragment:printPay");
                onRefundSuccess();      // 无论打印成功还是失败,该退款已经成功,应该正常返回
                if (handler.isSuccess() && handler.getData()) {
                    showToast("打印完成");
                } else {
                    showToast("打印失败");
                }
            }
        }
    }
}
