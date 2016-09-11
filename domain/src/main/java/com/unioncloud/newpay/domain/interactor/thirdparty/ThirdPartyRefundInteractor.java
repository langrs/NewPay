package com.unioncloud.newpay.domain.interactor.thirdparty;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyRefundOrder;
import com.unioncloud.newpay.domain.repository.ThirdPartyRepository;

import rx.Observable;

/**
 * Created by cwj on 16/9/1.
 */
public class ThirdPartyRefundInteractor extends BaseInteractor<ThirdPartyRefundOrder> {

    ThirdPartyRepository repository;
    ThirdPartyRefundOrder order;
    int totalFee;       // 原单总金额

    public ThirdPartyRefundInteractor(ExecutorProvider provider,
                                      ThirdPartyRefundOrder order,
                                      int totalFee,
                                      ThirdPartyRepository repository) {
        super(provider);
        this.order = order;
        this.totalFee = totalFee;
        this.repository = repository;
    }

    @Override
    protected Observable<ThirdPartyRefundOrder> bindObservable() {
        return repository.refund(order.getOrderId(), order.getThirdOrderId(), totalFee,
                order.getRefundOrderId(), order.getRefundFee());
    }
}
