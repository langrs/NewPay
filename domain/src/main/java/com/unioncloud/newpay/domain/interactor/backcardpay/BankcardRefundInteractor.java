package com.unioncloud.newpay.domain.interactor.backcardpay;

import android.util.Log;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundRequest;
import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundResult;
import com.unioncloud.newpay.domain.repository.BankcardTradeRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/23.
 */
public class BankcardRefundInteractor extends BaseInteractor<BankcardRefundResult> {

    private BankcardTradeRepository repository;
    private BankcardRefundRequest request;

    public BankcardRefundInteractor(ExecutorProvider provider,
                                    BankcardRefundRequest request,
                                    BankcardTradeRepository repository) {
        super(provider);
        this.request = request;
        this.repository = repository;
    }

    @Override
    protected Observable<BankcardRefundResult> bindObservable() {
//        return repository.saleVoid(request);    // 实际调用的是"当日撤销"
        return  repository.refund(request);
    }
}
