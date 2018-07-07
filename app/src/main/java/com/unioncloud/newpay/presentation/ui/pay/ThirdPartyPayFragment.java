package com.unioncloud.newpay.presentation.ui.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.esummer.android.utils.DeviceUtils;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.SwiftPassConst;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.print.PrintThirdPartyOrder;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyOrder;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.payment.ThirdPartyPayHandler;
import com.unioncloud.newpay.presentation.presenter.print.PrintThirdPartyHandler;
import com.unioncloud.newpay.presentation.presenter.sharedpreferences.PreferencesUtils;
import com.zbar.scan.ScanCaptureActivity;

/**
 * Created by cwj on 16/8/15.
 */
public class ThirdPartyPayFragment extends PayFragment {

    public static ThirdPartyPayFragment newWechatPay() {
        return newWechatPay(false);
    }

    public static ThirdPartyPayFragment newAliPay() {
        return newAliPay(false);
    }

    public static ThirdPartyPayFragment newWechatPay(boolean isFillIn) {
        ThirdPartyPayFragment fragment = new ThirdPartyPayFragment();
        fragment.setPayType(PAY_WECHAT);
        fragment.getArguments().putBoolean("isFillIn", isFillIn);
        return fragment;
    }

    public static ThirdPartyPayFragment newAliPay(boolean isFillIn) {
        ThirdPartyPayFragment fragment = new ThirdPartyPayFragment();
        fragment.setPayType(PAY_ALI);
        fragment.getArguments().putBoolean("isFillIn", isFillIn);
        return fragment;
    }

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
    ThirdPartyPayHandler handler;

    private void setPayType(int payType) {
        getArguments().putInt("thirdPartyPayType", payType);
    }

    private int getPayType() {
        return getArguments().getInt("thirdPartyPayType");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        handler = getSaveHandler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initFillInView(view);
        return view;
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
        if (isFillIn()) {
            if (TextUtils.isEmpty(fillInRelationNoEt.getText())) {
                showToast("补录必须输入原微信/支付宝支付单号");
                return;
            }
            fillInPay(willPay);
        } else {
            startScan(willPay);
        }
    }

    private void fillInPay(int payAmount) {
        Payment payment = getPayment();
        PaymentUsed used = new PaymentUsed();
        used.setPaymentId(payment.getPaymentId());
        used.setPaymentName(payment.getPaymentName());
        used.setPaymentQy(payment.getPaymentQy());
        used.setPayAmount(payAmount);
        used.setExcessAmount(0);
        used.setCurrencyId(payment.getCurrencyId());
        used.setExchangeRate(payment.getExchangeRate());
        used.setRelationNumber(fillInRelationNoEt.getText().toString()); // 第三方的单号,太长了.不存
        CheckoutDataManager.getInstance().usePayment(used);

        onPaidSuccess();
    }

    private void startScan(int willPay) {
        if (!hasWillPayAmount()) {
            setWillPayAmount(willPay);

            String amount = MoneyUtils.fenToString(willPay);
            Intent intent = ScanCaptureActivity.getStartIntent(getActivity(), "支付金额:" + amount);
            startActivityForResult(intent, REQUEST_SCAN_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SCAN_CODE) {
            Log.e("ThirdPartyPayFragment", "onActivityResult");
            if (resultCode == Activity.RESULT_OK) {
                String code = data.getStringExtra("SCAN_RESULT");
                if (isMatchQrCode(code)) {
                    getArguments().putString("scan_code", code);
                } else {
                    getArguments().remove("willPay");
                    getArguments().remove("scan_code");
                    showToast("请确认扫描的二维码是" + getPaymentSignpost().getName() + "!");
                }
            } else {
                getArguments().remove("willPay");
                getArguments().remove("scan_code");
                showToast("取消扫描");
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isMatchQrCode(String code) {
        if (getPaymentSignpost() == PaymentSignpost.ALI) {
            return isAliQrCode(code);
        }
        if (getPaymentSignpost() == PaymentSignpost.WECHAT) {
            return isWchatQrCode(code);
        }
        return false;
    }

    private boolean isAliQrCode(String code) {
        return code.startsWith("28");
    }

    private boolean isWchatQrCode(String code) {
        return code.startsWith("13");
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
        performPayWhenRestored();
    }

    private void performPayWhenRestored() {
        if (!isPaying()) {
            return;
        }
        ThirdPartyPayHandler payHandler = getPayingHandler();
        if (payHandler != null) {
            dealPay(payHandler);
        }
    }

    private ThirdPartyPayHandler getPayingHandler() {
        return (ThirdPartyPayHandler) CheckoutDataManager.getInstance().getPaying(getPayment().getPaymentId());
    }

    private void thirdPartyPay(String code) {
        PosInfo posInfo = PosDataManager.getInstance().getPosInfo();

        ThirdPartyOrder order = new ThirdPartyOrder();
        order.setOrderId(CheckoutDataManager.getInstance().createOrderId());
        order.setTotalFee(getWillPayAmount());
        removeWillPay();
        order.setAttach(getPaymentSignpost().getName());
        order.setBody(posInfo.getShopName() + posInfo.getStoreName());
        if (handler == null) {
            handler = new ThirdPartyPayHandler(order,
                    getPaymentSignpost().numberToInt(), code,
                    DeviceUtils.getLocalIpAddress());
        }
        CheckoutDataManager.getInstance().addPaying(getPayment().getPaymentId(), handler);
        dealPay(handler);   // 更新UI中的转圈
        handler.startPay();
    }

    private void dealPay(ThirdPartyPayHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog();
                setPaying();
                handler.callOrSubscribe(payListener);
            } else {
                removePaying();
                handler.removeCompletionListener(payListener);
                CheckoutDataManager.getInstance().removePaying(getPayment().getPaymentId());
                if (handler.isSuccess() && handler.getData().isSuccess()) {
                    ThirdPartyOrder order = handler.getData().getData();
                    saveLastPay(order);
                    print(order);
                } else {
                    dismissProgressDialog();
                    showToast(handler.getData().getErrorMessage());
                }
            }
        }
    }

    private void saveLastPay(ThirdPartyOrder order) {
        PreferencesUtils.saveLastThirdPartyPayId(getContext().getApplicationContext(), order.getThirdOrderId());
    }

    private void print(ThirdPartyOrder order) {
        PrintThirdPartyOrder printInfo = new PrintThirdPartyOrder();
//        printInfo.setOrderTitle(getPaymentSignpost().getName() + "签购单");
        printInfo.setOrderTitle(order.getThirdTradeName() + "签购单");
        PosInfo posInfo = PosDataManager.getInstance().getPosInfo();
        printInfo.setShopName("合胜百货");
        printInfo.setStoreName(posInfo.getShopName());
        printInfo.setPosNo(posInfo.getPosNumber());
        printInfo.setUserNo(posInfo.getUserNumber());
        printInfo.setThirdPartyOrderId(order.getThirdOrderId());
        printInfo.setDate(order.getDatetime());
        printInfo.setAmount(MoneyUtils.fenToString(order.getTotalFee()));
        printInfo.setMchId(SwiftPassConst.MCH_ID); // 第三方支付的商户号
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


    private ThirdPartyPayHandler getSaveHandler() {
        return (ThirdPartyPayHandler) getSavedState().remove("ThirdPartyPayFragment:handler");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getSavedState().saveState("ThirdPartyPayFragment:handler", handler);
        super.onSaveInstanceState(outState);
    }


    private void setPaying() {
        getArguments().putBoolean("ThirdPartyPayFragment:paying", true);
    }

    private boolean isPaying() {
        return getArguments().getBoolean("ThirdPartyPayFragment:paying");
    }

    private void removePaying() {
        getArguments().remove("ThirdPartyPayFragment:paying");
    }

    private boolean hasWillPayAmount() {
        return getArguments().containsKey("willPay");
    }

    private void setWillPayAmount(int willPay) {
        getArguments().putInt("willPay", willPay);
    }

    private void removeWillPay() {
        getArguments().remove("willPay");
    }

    private int getWillPayAmount() {
        return getArguments().getInt("willPay");
    }

    private Payment payment;
    private Payment getPayment() {
        if (payment == null) {
            payment = PosDataManager.getInstance().getPaymentByNumberInt(
                    getPaymentSignpost().numberToInt());
        }
        return payment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroy();
        if (isPaying()) {
            getPayingHandler().removeCompletionListener(payListener);
        }
    }
}
