package com.unioncloud.newpay.presentation.ui.pay;

import android.os.Bundle;

import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleRequest;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleResult;
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
        BankcardFragment fragment = new BankcardFragment();
        return fragment;
    }

    @Override
    protected PaymentSignpost getPaymentSignpost() {
        return PaymentSignpost.BANKCARD;
    }

    @Override
    protected void pay(int unpay, int willPay) {
        if (willPay > unpay) {
            showToast("支付金额已超出未付款项");
            return;
        }
        BankcardSaleRequest request = new BankcardSaleRequest();
        request.setTransAmount(willPay);
        BankcardPayHandler handler = new BankcardPayHandler(request);
        updateForResponse("BankcardFragment:pay", handler, payHandlerListener);
        handler.run();
    }

    private void dealPay(BankcardPayHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if(handler.isUpdating()) {
                showProgressDialog();
                handler.addCompletionListener(payListener);
            } else {
                if (isVisible()) {
                    dismissProgressDialog();
                    cleanupResponse("BankcardFragment:pay");
                    if (handler.isSuccess() && handler.getData().isSuccess()) {
                        showToastLong("银行卡刷新返回成功");
                        paySuccess(handler.getData().getData());
                    } else {
                        showToastLong(handler.getData().getErrorMessage());
                    }
                }
            }
        }
    }

    private void paySuccess(BankcardSaleResult result) {
//        dismiss();
        onPaidSuccess();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateForResponse("BankcardFragment:pay");
    }

}
