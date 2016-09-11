package com.unioncloud.newpay.domain.interactor.vip;

import android.text.TextUtils;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.erp.VipCard;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.repository.VipRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/14.
 */
public class QueryVipCardInteractor extends BaseInteractor<VipCard> {

    private String cdInfo;
    private String billNo;
    private PosInfo posInfo;
    private VipRepository repository;

    public QueryVipCardInteractor(ExecutorProvider provider,
                                   String cdInfo,
                                   String billNo,
                                   PosInfo posInfo,
                                   VipRepository repository) {
        super(provider);
        this.cdInfo = cdInfo;
        this.billNo = billNo;
        this.posInfo = posInfo;
        this.repository = repository;
    }

    @Override
    protected Observable<VipCard> bindObservable() {
        if (!TextUtils.isEmpty(cdInfo)) {
            return repository.queryByCd(cdInfo, posInfo);
        } else {
            return repository.queryByBillNo(billNo, posInfo);
        }
    }
}
