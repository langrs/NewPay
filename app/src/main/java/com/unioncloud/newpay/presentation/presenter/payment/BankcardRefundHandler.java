package com.unioncloud.newpay.presentation.presenter.payment;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.NewPayApplication;
import com.unioncloud.newpay.domain.interactor.backcardpay.BankcardRefundInteractor;
import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundRequest;
import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundResult;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.presentation.model.ResultData;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.model.refund.RefundDataManager;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;
import com.unioncloud.newpay.presentation.ui.pay.PaymentSignpost;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/23.
 */
public class BankcardRefundHandler extends UpdateHandler<ResultData<BankcardRefundResult>, BankcardRefundHandler>
        implements Runnable {

    BankcardRefundRequest request;

    public BankcardRefundHandler(BankcardRefundRequest request) {
        super(new ResultData<BankcardRefundResult>(), true);
        this.request = request;
    }

    @Override
    public void run() {
        BankcardRefundInteractor interactor = new BankcardRefundInteractor(
                PresenterUtils.getExecutorProvider(),
                request,
                PresenterUtils.getBankcardPayRepository(NewPayApplication.getInstance()));
        interactor.execute(new Subscriber<BankcardRefundResult>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(BankcardRefundResult result) {
                data.onSuccess(result);

                Payment payment = PosDataManager.getInstance().getPaymentByNumberInt(
                        PaymentSignpost.BANKCARD.numberToInt());
                PaymentUsed used = new PaymentUsed();
                used.setPaymentId(payment.getPaymentId());
                used.setPaymentName(payment.getPaymentName());
                used.setPaymentQy(payment.getPaymentQy());
                used.setPayAmount(0 - request.getTransAmount());
                used.setExcessAmount(0);
                used.setCurrencyId(payment.getCurrencyId());
                used.setExchangeRate(payment.getExchangeRate());
                RefundDataManager.getInstance().usePayment(used);
                onUpdateCompleted();
            }
        });
    }
}
