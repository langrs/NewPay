package com.unioncloud.newpay.domain.repository;

import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 16/8/8.
 */
public interface PrePayRepository {

    Observable<List<Object>> prePay();

}
