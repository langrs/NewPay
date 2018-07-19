package com.unioncloud.newpay.domain.repository;

import com.unioncloud.newpay.domain.model.testp.Testp;

import rx.Observable;
import rx.subscriptions.BooleanSubscription;

public interface TestpRepository {
//    查詢會員卡
    Observable<Testp> queryByNo(Testp testp);
//    更新会员卡
    Observable<Boolean> update(Testp testp);
}