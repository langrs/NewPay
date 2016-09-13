package com.unioncloud.newpay.domain.interactor.notice;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.notice.Notice;
import com.unioncloud.newpay.domain.repository.NoticeRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 16/9/12.
 */
public class QueryNoticeInteractor extends BaseInteractor<List<Notice>> {

    NoticeRepository repository;
    String shopId;

    public QueryNoticeInteractor(ExecutorProvider provider,
                                 String shopId,
                                 NoticeRepository repository) {
        super(provider);
        this.shopId = shopId;
        this.repository = repository;
    }

    @Override
    protected Observable<List<Notice>> bindObservable() {
        return repository.queryAllNotice(shopId);
    }
}
