package com.unioncloud.newpay.domain.interactor.gift;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.erp.GiftCard;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.repository.GiftRepository;

import rx.Observable;

/**
 * Created by cwj on 16/9/3.
 */
public class QueryGiftCardInteractor extends BaseInteractor<GiftCard> {

    private String track;
    private PosInfo posInfo;
    private GiftRepository repository;

    public QueryGiftCardInteractor(ExecutorProvider provider,
                                   String track,
                                   PosInfo posInfo,
                                   GiftRepository repository) {
        super(provider);
        this.track = track;
        this.posInfo = posInfo;
        this.repository = repository;
    }

    @Override
    protected Observable<GiftCard> bindObservable() {
        return repository.queryGiftCard(track, posInfo);
    }
}
