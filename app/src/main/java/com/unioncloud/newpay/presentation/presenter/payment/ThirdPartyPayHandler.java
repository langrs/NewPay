package com.unioncloud.newpay.presentation.presenter.payment;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.thirdparty.ThirdPartyPayInteractor;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyOrder;
import com.unioncloud.newpay.presentation.model.ResultData;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;
import com.unioncloud.newpay.presentation.ui.pay.PaymentSignpost;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/18.
 */
public class ThirdPartyPayHandler extends UpdateHandler<ResultData<ThirdPartyOrder>, ThirdPartyPayHandler>
        implements Runnable {

    private String barcode;
    private String ip;
    private int paymentNumberInt;

    public ThirdPartyPayHandler(ThirdPartyOrder data, int paymentNumberInt, String barcode, String ip) {
        super(new ResultData<ThirdPartyOrder>(false, data, "支付失败"), true);
        this.barcode = barcode;
        this.ip = ip;
        this.paymentNumberInt = paymentNumberInt;
    }

    @Override
    public void run() {
        ThirdPartyPayInteractor interactor = new ThirdPartyPayInteractor(
                PresenterUtils.getExecutorProvider(),
                data.getData().getOrderId(),
                barcode,
                data.getData().getAttach(),
                data.getData().getTotalFee(),
                ip,
                data.getData().getBody(),
                PresenterUtils.getThirdPartyRepository());
        interactor.execute(new Subscriber<ThirdPartyOrder>() {
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
            public void onNext(ThirdPartyOrder thirdPartyOrder) {
                ThirdPartyOrder order = data.getData();
                order.setThirdOrderId(thirdPartyOrder.getThirdOrderId());
                order.setAttach(thirdPartyOrder.getAttach());
                order.setBillNo(thirdPartyOrder.getBillNo());
                order.setDatetime(thirdPartyOrder.getDatetime());
                order.setTradeState(thirdPartyOrder.getTradeState());
                order.setTotalFee(thirdPartyOrder.getTotalFee());
                order.setCouponFee(thirdPartyOrder.getCouponFee());
                data.onSuccess(thirdPartyOrder);

                Payment payment = PosDataManager.getInstance().getPaymentByNumberInt(
                        paymentNumberInt);
                PaymentUsed used = new PaymentUsed();
                used.setPaymentId(payment.getPaymentId());
                used.setPaymentName(payment.getPaymentName());
                used.setPaymentQy(payment.getPaymentQy());
                used.setPayAmount(data.getData().getTotalFee());
                used.setExcessAmount(0);
                used.setCurrencyId(payment.getCurrencyId());
                used.setExchangeRate(payment.getExchangeRate());
                used.setRelationNumber(data.getData().getThirdOrderId()); // 第三方的单号,太长了.不存

                CheckoutDataManager.getInstance().usePayment(used);

                onUpdateCompleted();
            }
        });
    }
}
