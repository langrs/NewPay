package com.unioncloud.newpay.data.repository.testp.datasource;

import com.unioncloud.newpay.data.entity.testp.TestpEntity;
import com.unioncloud.newpay.domain.model.testp.Testp;

import rx.Observable;
import rx.subscriptions.BooleanSubscription;

public interface TestpStore {
    Observable<TestpEntity> queryByNo(String ckcode);

    Observable<Boolean> update(Testp testp);

}
