package com.unioncloud.newpay.domain.repository;

import com.unioncloud.newpay.domain.model.print.PrintOrderInfo;

import rx.Observable;

/**
 * Created by cwj on 16/8/19.
 */
public interface PrintRepository {

    Observable<Boolean> printOrder(PrintOrderInfo orderInfo);

}
