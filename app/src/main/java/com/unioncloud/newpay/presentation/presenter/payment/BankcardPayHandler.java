package com.unioncloud.newpay.presentation.presenter.payment;

import com.unioncloud.newpay.NewPayApplication;
import com.unioncloud.newpay.domain.interactor.backcardpay.BankcardSaleInteractor;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleRequest;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleResult;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.presentation.model.ResultData;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;
import com.unioncloud.newpay.presentation.ui.pay.PaymentSignpost;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/17.
 */
public class BankcardPayHandler extends PayHandler<ResultData<BankcardSaleResult>, BankcardPayHandler> {

    private BankcardSaleRequest request;

    public BankcardPayHandler(BankcardSaleRequest request) {
        super(new ResultData<BankcardSaleResult>(), true);
        this.request = request;
    }

    @Override
    public void startPay() {
        BankcardSaleInteractor interactor = new BankcardSaleInteractor(
                PresenterUtils.getExecutorProvider(),
                request,
                PresenterUtils.getBankcardPayRepository(NewPayApplication.getInstance()));
        interactor.execute(new Subscriber<BankcardSaleResult>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                data.onFailed(e.getMessage());
                onUpdateFailed();
            }

            @Override
            public void onNext(BankcardSaleResult paxSaleResult) {
                data.onSuccess(paxSaleResult);

                Payment payment = PosDataManager.getInstance().getPaymentByNumberInt(
                        PaymentSignpost.BANKCARD.numberToInt());
                PaymentUsed used = new PaymentUsed();
                used.setPaymentId(payment.getPaymentId());
                used.setPaymentName(payment.getPaymentName());
                used.setPaymentQy(payment.getPaymentQy());
                used.setPayAmount(request.getTransAmount());
                used.setExcessAmount(0);
                used.setCurrencyId(payment.getCurrencyId());
                used.setExchangeRate(payment.getExchangeRate());
                used.setRelationNumber(paxSaleResult.getCardNo());  // 关联号码(银行)
                CheckoutDataManager.getInstance().usePayment(used);

                onUpdateCompleted();
            }
        });
    }
}
