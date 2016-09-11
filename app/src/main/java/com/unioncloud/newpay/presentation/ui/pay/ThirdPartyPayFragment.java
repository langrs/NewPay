package com.unioncloud.newpay.presentation.ui.pay;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.esummer.android.utils.DeviceUtils;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.print.PrintThirdPartyOrder;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyOrder;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.payment.ThirdPartyPayHandler;
import com.unioncloud.newpay.presentation.presenter.print.PrintThirdPartyHandler;
import com.zbar.scan.ScanCaptureActivity;

/**
 * Created by cwj on 16/8/15.
 */
public class ThirdPartyPayFragment extends PayFragment {

    public static ThirdPartyPayFragment newWechatPay() {
        ThirdPartyPayFragment fragment = new ThirdPartyPayFragment();
        fragment.setPayType(PAY_WECHAT);
        return fragment;
    }

    public static ThirdPartyPayFragment newAliPay() {
        ThirdPartyPayFragment fragment = new ThirdPartyPayFragment();
        fragment.setPayType(PAY_ALI);
        return fragment;
    }

    private static StateUpdateHandlerListener<ThirdPartyPayFragment, ThirdPartyPayHandler> payHandlerListener =
            new StateUpdateHandlerListener<ThirdPartyPayFragment, ThirdPartyPayHandler>() {
                @Override
                public void onUpdate(String key, ThirdPartyPayFragment handler, ThirdPartyPayHandler response) {
                    handler.dealPay(response);
                }

                @Override
                public void onCleanup(String key, ThirdPartyPayFragment handler, ThirdPartyPayHandler response) {
                    response.removeCompletionListener(handler.payListener);
                }
            };
    private UpdateCompleteCallback<ThirdPartyPayHandler> payListener =
            new UpdateCompleteCallback<ThirdPartyPayHandler>() {
        @Override
        public void onCompleted(ThirdPartyPayHandler response, boolean isSuccess) {
            dealPay(response);
        }
    };

    private static StateUpdateHandlerListener<ThirdPartyPayFragment, PrintThirdPartyHandler> printHandlerListener =
            new StateUpdateHandlerListener<ThirdPartyPayFragment, PrintThirdPartyHandler>() {
                @Override
                public void onUpdate(String key, ThirdPartyPayFragment handler, PrintThirdPartyHandler response) {
                    handler.dealPrint(response);
                }

                @Override
                public void onCleanup(String key, ThirdPartyPayFragment handler, PrintThirdPartyHandler response) {
                    response.removeCompletionListener(handler.printListener);
                }
            };
    private UpdateCompleteCallback<PrintThirdPartyHandler> printListener = new UpdateCompleteCallback<PrintThirdPartyHandler>() {
        @Override
        public void onCompleted(PrintThirdPartyHandler response, boolean isSuccess) {
            dealPrint(response);
        }
    };

    private static final int PAY_WECHAT = 1;
    private static final int PAY_ALI = 2;
    private static final int REQUEST_SCAN_CODE = 1000;

    private void setPayType(int payType) {
        getArguments().putInt("thirdPartyPayType", payType);
    }

    private int getPayType() {
        return getArguments().getInt("thirdPartyPayType");
    }

    @Override
    protected PaymentSignpost getPaymentSignpost() {
        int payType = getPayType();
        switch (payType) {
            case PAY_ALI:
                return PaymentSignpost.ALI;
            case PAY_WECHAT:
                return PaymentSignpost.WECHAT;
        }
        return null;
    }

    @Override
    protected void pay(int unpay, int willPay) {
        if (willPay > unpay) {
            showToast("支付金额超出未付款项");
            return;
        }
        startScan(willPay);
    }

    private void startScan(int willPay) {
        if (!isPaying()) {
            getArguments().putInt("willPay", willPay);

            String amount = MoneyUtils.fenToString(willPay);
            Intent intent = ScanCaptureActivity.getStartIntent(getActivity(), "支付金额:" + amount);
            startActivityForResult(intent, REQUEST_SCAN_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SCAN_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String code = data.getStringExtra("SCAN_RESULT");
                getArguments().putString("scan_code", code);
            } else {
                getArguments().remove("willPay");
                showToast("取消扫描");
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
                thirdPartyPay(code);
            }
        }
    }

    private void thirdPartyPay(String code) {
        PosInfo posInfo = PosDataManager.getInstance().getPosInfo();

        ThirdPartyOrder order = new ThirdPartyOrder();
        order.setOrderId(CheckoutDataManager.getInstance().createOrderId());
        order.setTotalFee(getWillPay());
        order.setAttach(getPaymentSignpost().getName());
        order.setBody(posInfo.getShopName() + posInfo.getStoreName());
        ThirdPartyPayHandler handler = new ThirdPartyPayHandler(order,
                getPaymentSignpost().numberToInt(), code,
                    DeviceUtils.getLocalIpAddress());
        updateForResponse("ThirdPartyPayFragment:pay", handler, payHandlerListener);
        handler.run();
    }

    private void dealPay(ThirdPartyPayHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog();
                handler.addCompletionListener(payListener);
            } else {
                if (handler.isSuccess() && handler.getData().isSuccess()) {
                    print(handler.getData().getData());
                } else {
                    dismissProgressDialog();
                    showToast(handler.getData().getErrorMessage());
                }
                cleanupResponse("ThirdPartyPayFragment:pay");
                removeWillPay();
            }
        }
    }

    private void print(ThirdPartyOrder order) {
        PrintThirdPartyOrder printInfo = new PrintThirdPartyOrder();
        printInfo.setOrderTitle(getPaymentSignpost().getName() + "签购单");
        PosInfo posInfo = PosDataManager.getInstance().getPosInfo();
        printInfo.setShopName("合胜百货");
        printInfo.setStoreName(posInfo.getShopName());
        printInfo.setPosNo(posInfo.getPosNumber());
        printInfo.setUserNo(posInfo.getUserNumber());
        printInfo.setThirdPartyOrderId(order.getThirdOrderId());
        printInfo.setDate(order.getDatetime());
        printInfo.setAmount(MoneyUtils.fenToString(order.getTotalFee()));
        printInfo.setMchId("7551000001"); // 第三方支付的商户号
        printInfo.setTradeType("消费");

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
                String payName = getPaymentSignpost().getName();

                showProgressDialog("正在打印"+ payName + "签购单...");
                handler.addCompletionListener(printListener);
            } else {
                dismissProgressDialog();
                cleanupResponse("ThirdPartyPayFragment:printPay");
                onPaidSuccess();
                String payName = getPaymentSignpost().getName();
                if (handler.isSuccess() && handler.getData()) {
                    showToast("打印" + payName + "签购单完成");
                } else {
                    showToast("打印" + payName + "签购单失败");
                }
            }
        }
    }

    private boolean isPaying() {
        return getArguments().containsKey("willPay");
    }

    private void setWillPay(int willPay) {
        getArguments().putInt("willPay", willPay);
    }

    private void removeWillPay() {
        getArguments().remove("willPay");
    }

    private int getWillPay() {
        return getArguments().getInt("willPay");
    }
}
