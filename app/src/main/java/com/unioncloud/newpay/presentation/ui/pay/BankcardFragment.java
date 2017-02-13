package com.unioncloud.newpay.presentation.ui.pay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private Payment payment;

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
            BankcardPayHandler handler = new BankcardPayHandler(request);
            CheckoutDataManager.getInstance().addPaying(getPayment().getPaymentId(), handler);
            dealPay(handler);   // 更新UI中的转圈
            handler.startPay();
        }
    }

    private void fillInPay(int willPay) {
        Payment payment = getPayment();
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
                handler.callOrSubscribe(payListener);
            } else {
                dismissProgressDialog();
                handler.removeCompletionListener(payListener);
                removePaying();
                CheckoutDataManager.getInstance().removePaying(getPayment().getPaymentId());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isPaying()) {
            getPayingHandler().removeCompletionListener(payListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        performPayWhenRestored();
    }

    private void performPayWhenRestored() {
        if (!isPaying()) {
            return;
        }
        BankcardPayHandler payHandler = getPayingHandler();
        if (payHandler != null) {
            dealPay(payHandler);
        }
    }

    private BankcardPayHandler getPayingHandler() {
        return (BankcardPayHandler) CheckoutDataManager.getInstance().getPaying(getPayment().getPaymentId());
    }

    private Payment getPayment() {
        if (payment == null) {
            payment = PosDataManager.getInstance().getPaymentByNumberInt(
                    PaymentSignpost.BANKCARD.numberToInt());
        }
        return payment;
    }
}
