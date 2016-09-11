package com.unioncloud.newpay.presentation.presenter.payment;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.thirdparty.ThirdPartyRefundInteractor;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.model.thirdparty.RefundState;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyRefundOrder;
import com.unioncloud.newpay.presentation.model.ResultData;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.model.refund.RefundDataManager;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/9/1.
 */
public class ThirdPartyRefundHandler extends UpdateHandler<ResultData<ThirdPartyRefundOrder>, ThirdPartyRefundHandler>
        implements Runnable {
    private int totalFee;
    private int paymentNumberInt;

    public ThirdPartyRefundHandler(ThirdPartyRefundOrder data, int totalFee, int paymentNumberInt) {
        super(new ResultData<ThirdPartyRefundOrder>(false, data, "退款失败"), true);
        this.totalFee = totalFee;
        this.paymentNumberInt = paymentNumberInt;
    }

    @Override
    public void run() {
        ThirdPartyRefundInteractor interactor = new ThirdPartyRefundInteractor(
                PresenterUtils.getExecutorProvider(),
                data.getData(),
                totalFee,
                PresenterUtils.getThirdPartyRepository());
        interactor.execute(new Subscriber<ThirdPartyRefundOrder>() {
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
            public void onNext(ThirdPartyRefundOrder refundOrder) {
                ThirdPartyRefundOrder order = data.getData();
                order.setRefundState(RefundState.SUCCESS);
                order.setThirdRefundId(refundOrder.getThirdRefundId());
                order.setRefundChannel(refundOrder.getRefundChannel());
                order.setCouponRefundFee(refundOrder.getCouponRefundFee());
                data.onSuccess(order);

                Payment payment = PosDataManager.getInstance().getPaymentByNumberInt(
                        paymentNumberInt);
                PaymentUsed used = new PaymentUsed();
                used.setPaymentId(payment.getPaymentId());
                used.setPaymentName(payment.getPaymentName());
                used.setPaymentQy(payment.getPaymentQy());
                used.setPayAmount(-data.getData().getRefundFee());
                used.setExcessAmount(0);
                used.setCurrencyId(payment.getCurrencyId());
                used.setExchangeRate(payment.getExchangeRate());
                used.setRelationNumber(data.getData().getThirdRefundId()); // 第三方的单号,太长了.不存

                RefundDataManager.getInstance().usePayment(used);
                onUpdateCompleted();
            }
        });
    }
}
