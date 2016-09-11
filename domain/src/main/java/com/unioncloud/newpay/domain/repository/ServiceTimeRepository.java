package com.unioncloud.newpay.domain.repository;

import com.unioncloud.newpay.domain.model.ServiceTime;

import rx.Observable;

/**
 * Created by cwj on 16/8/8.
 */
public interface ServiceTimeRepository {

    Observable<ServiceTime> getServiceTime();

}
