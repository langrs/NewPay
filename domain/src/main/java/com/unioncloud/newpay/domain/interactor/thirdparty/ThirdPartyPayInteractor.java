package com.unioncloud.newpay.domain.interactor.thirdparty;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyOrder;
import com.unioncloud.newpay.domain.repository.ThirdPartyRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/12.
 */
public class ThirdPartyPayInteractor extends BaseInteractor<ThirdPartyOrder> {

    private ThirdPartyRepository repository;
    private String orderId;
    private String code;
    private String attach;
    private int total;
    private String ip;
    private String body;

    public ThirdPartyPayInteractor(ExecutorProvider provider,
                                   String orderId, String code, String attach,
                                   int total, String ip, String body,
                                   ThirdPartyRepository repository) {
        super(provider);
        this.orderId = orderId;
        this.code = code;
        this.attach = attach;
        this.ip = ip;
        this.total = total;
        this.body = body;
        this.repository = repository;
    }

    @Override
    protected Observable<ThirdPartyOrder> bindObservable() {
        return repository.pay(orderId, code, attach, total, ip, body);
    }
}
