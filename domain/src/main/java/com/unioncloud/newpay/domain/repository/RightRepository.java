package com.unioncloud.newpay.domain.repository;

import rx.Observable;

/**
 * Created by cwj on 16/9/13.
 */
public interface RightRepository {

    Observable<Boolean> queryRefundRight(String number, String password);

}
