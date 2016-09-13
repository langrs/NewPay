package com.unioncloud.newpay.data.repository.right.datasource;

import rx.Observable;

/**
 * Created by cwj on 16/9/13.
 */
public interface RightStore {

    Observable<Boolean> queryRefundRight(String userNo, String password);

}
