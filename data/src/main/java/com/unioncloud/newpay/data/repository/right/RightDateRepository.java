package com.unioncloud.newpay.data.repository.right;

import com.unioncloud.newpay.data.repository.StoreFactory;
import com.unioncloud.newpay.domain.repository.RightRepository;

import rx.Observable;

/**
 * Created by cwj on 16/9/13.
 */
public class RightDateRepository implements RightRepository {

    @Override
    public Observable<Boolean> queryRefundRight(String number, String password) {
        return StoreFactory.getRightStore().queryRefundRight(number, password);
    }
}
