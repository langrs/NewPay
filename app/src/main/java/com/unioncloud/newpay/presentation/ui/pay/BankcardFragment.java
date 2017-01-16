package com.unioncloud.newpay.presentation.ui.pay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleRequest;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleResult;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.payment.BankcardPayHandler;

/**
 * Created by cwj on 16/8/17.
 */
public class BankcardFragment extends PayFragment {

    private static StateUpdateHandlerListener<BankcardFragment, BankcardPayHandler> payHandlerListener =
            new StateUpdateHandlerListener<BankcardFragment, BankcardPayHandler>() {
                @Override
                public void onUpdate(String key, BankcardFragment handler, BankcardPayHandler response) {
                    handler.dealPay(response);
                }

                @Override
                public void onCleanup(String key, BankcardFragment handler, BankcardPayHandler response) {
                    response.removeCompletionListener(handler.payListener);
                }
            };

    private UpdateCompleteCallback<BankcardPayHandler> payListener =
            new UpdateCompleteCallback<BankcardPayHandler>() {
                @Override
                public void onCompleted(BankcardPayHandler response, boolean isSuccess) {
                    dealPay(response);
                }
            };


    public static BankcardFragment newInstance() {
        return newInstance(false);
    }

    public static BankcardFragment newInstance(boolean isFillIn) {
        BankcardFragment fragment = new BankcardFragment();
        fragment.getArguments().putBoolean("isFillIn", isFillIn);
        return fragment;
    }

    public static final String TAG_PAYING = "BankcardFragment:bankPaying";
    public static final String TAG_CALL_PAY = "BankcardFragment:pay";

    BankcardPayHandler handler;
    boolean isSavedPayHandler = false;

    @Override
    protected PaymentSignpost getPaymentSignpost() {
        return PaymentSignpost.BANKCARD;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
        initFillInView(view);
        return view;
    }

    @Override
    protected void pay(int unpay, int willPay) {
        if (willPay > unpay) {
            showToast("支付金额已超出未付款项");
            return;
        }
        if (isFillIn()) {
            if (TextUtils.isEmpty(fillInRelationNoEt.getText())) {
                showToast("补录必须录入原银行卡号");
                return;
            }
            fillInPay(willPay);
        } else {
            BankcardSaleRequest request = new BankcardSaleRequest();
            request.setTransAmount(willPay);
            handler = new BankcardPayHandler(request);
            updateForResponse(TAG_CALL_PAY, handler, payHandlerListener);
            handler.run();
        }
    }

    private void fillInPay(int willPay) {
        Payment payment = PosDataManager.getInstance().getPaymentByNumberInt(
                PaymentSignpost.BANKCARD.numberToInt());
        PaymentUsed used = new PaymentUsed();
        used.setPaymentId(payment.getPaymentId());
        used.setPaymentName(payment.getPaymentName());
        used.setPaymentQy(payment.getPaymentQy());
        used.setPayAmount(willPay);
        used.setExcessAmount(0);
        used.setCurrencyId(payment.getCurrencyId());
        used.setExchangeRate(payment.getExchangeRate());
        used.setRelationNumber(fillInRelationNoEt.getText().toString());  // 关联号码(银行)
        CheckoutDataManager.getInstance().usePayment(used);

        onPaidSuccess();
    }

    private void dealPay(BankcardPayHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog();
                setPaying();
                handler.addCompletionListener(payListener);
            } else {
                dismissProgressDialog();
                cleanupResponse(TAG_CALL_PAY);
                removePaying();
                if (handler.isSuccess() && handler.getData().isSuccess()) {
                    showToastLong("银行卡支付成功");
                    paySuccess(handler.getData().getData());
                } else {
                    showToastLong(handler.getData().getErrorMessage());
                }
            }
        }
    }

    private void setPaying() {
        getArguments().putBoolean(TAG_PAYING, true);
    }

    private boolean isPaying() {
        return getArguments().getBoolean(TAG_PAYING);
    }

    private void removePaying() {
        getArguments().remove(TAG_PAYING);
    }

    private void paySuccess(BankcardSaleResult result) {
        onPaidSuccess();
    }

    private BankcardPayHandler getSaveHandler() {
        return (BankcardPayHandler) getSavedState().remove("BankcardFragment:payHandler");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getSavedState().saveState("BankcardFragment:payHandler", handler);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected boolean retainUpdateHandler() {
//        return isPaying();
        return super.retainUpdateHandler();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        performPayWhenRestored();
    }

    private void performPayWhenRestoredOld() {
        if (isPaying()) {
            if (isSavedPayHandler) {
                updateForResponse(TAG_CALL_PAY, handler, payHandlerListener);
                handler = null;
            } else {
                updateForResponse(TAG_CALL_PAY);
            }
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
//        showToast("onViewStateRestored");
        restoreHandler();
    }

    private void restoreHandler() {
        handler = getSaveHandler();
        isSavedPayHandler = (handler != null);
//        if (isSavedPayHandler) {
//            showToast("savedInstanceState");
//        }
    }

    private void performPayWhenRestored() {
        if (!isPaying()) {
            return;
        }
        if (isSavedPayHandler) {
            updateForResponse(TAG_CALL_PAY);
        } else {
            updateForResponse(TAG_CALL_PAY, handler, payHandlerListener);
        }
        handler = null;
    }
}
