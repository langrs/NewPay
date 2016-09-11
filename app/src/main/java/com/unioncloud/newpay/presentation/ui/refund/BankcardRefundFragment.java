package com.unioncloud.newpay.presentation.ui.refund;

import android.text.TextUtils;

import com.esummer.android.dialog.DefaultDialogBuilder;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundRequest;
import com.unioncloud.newpay.presentation.presenter.payment.BankcardRefundHandler;
import com.unioncloud.newpay.presentation.ui.pay.PaymentSignpost;

/**
 * Created by cwj on 16/8/23.
 */
public class BankcardRefundFragment extends RefundFragment {

    public static BankcardRefundFragment newInstance() {
        BankcardRefundFragment fragment = new BankcardRefundFragment();
        return fragment;
    }

    @Override
    protected PaymentSignpost getPaymentSignpost() {
        return PaymentSignpost.BANKCARD;
    }

    private static StateUpdateHandlerListener<BankcardRefundFragment, BankcardRefundHandler> handlerListener =
            new StateUpdateHandlerListener<BankcardRefundFragment, BankcardRefundHandler>() {
                @Override
                public void onUpdate(String key, BankcardRefundFragment handler, BankcardRefundHandler response) {
                    handler.dealRefund(response);
                }

                @Override
                public void onCleanup(String key, BankcardRefundFragment handler, BankcardRefundHandler response) {
                    response.removeCompletionListener(handler.refundListener);
                }
            };
    private UpdateCompleteCallback<BankcardRefundHandler> refundListener =
            new UpdateCompleteCallback<BankcardRefundHandler>() {
        @Override
        public void onCompleted(BankcardRefundHandler response, boolean isSuccess) {
            dealRefund(response);
        }
    };

    @Override
    protected void refund() {
        String originalId = originalIdEt.getText().toString();
        if (TextUtils.isEmpty(originalId)) {
            showToast("原单号不能为空");
            return;
        }
        BankcardRefundRequest request = new BankcardRefundRequest();
        request.setManagerPwd("123456");
        request.setOriVoucherNo(originalId);
        request.setTransAmount(getRefundAmount());
        BankcardRefundHandler handler = new BankcardRefundHandler(request);
        updateForResponse("BankcardRefundFragment:refund", handler, handlerListener);
        handler.run();
    }

    private void dealRefund(BankcardRefundHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("银行卡退款...");
                handler.addCompletionListener(refundListener);
            } else {
                if (isVisible()) {
                    if (handler.isSuccess() && handler.getData().isSuccess()) {
                        onRefundSuccess();
                    } else {
                        showRetryRefund();
                    }
                    dismissProgressDialog();
                    cleanupResponse("BankcardRefundFragment:refund");
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateForResponse("BankcardRefundFragment:refund");
    }

    private static final int REQUEST_RETRY_REFUND_BANKCARD = 0X11;

    private void showRetryRefund() {
        DefaultDialogBuilder builder = createAndSaveDialogBuilder(REQUEST_RETRY_REFUND_BANKCARD);
        builder.setTitle("银行卡退款失败");
        builder.setMessage("是否重新提交银行卡退款?");
        builder.setCancelable(false);
        showDialog(builder);
    }

    @Override
    protected void onDialogCancelClicked(int dialogId) {
        if (dialogId == REQUEST_RETRY_REFUND_BANKCARD) {
            onRefundFailed();
        }
    }

    @Override
    protected void onDialogOkClicked(int dialogId) {
        if (dialogId == REQUEST_RETRY_REFUND_BANKCARD) {
            refund();
        }
    }
}
