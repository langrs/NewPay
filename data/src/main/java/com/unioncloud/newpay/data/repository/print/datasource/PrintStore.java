package com.unioncloud.newpay.data.repository.print.datasource;

import com.unioncloud.newpay.domain.model.print.PrintOrderInfo;

import rx.Observable;

/**
 * Created by cwj on 16/8/19.
 */
public interface PrintStore {

    Observable<Boolean> printOrder(PrintOrderInfo orderInfo);

}
