package com.unioncloud.newpay.domain.repository;

import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.pos.PosRegister;

import rx.Observable;

/**
 * Created by cwj on 16/6/27.
 */
public interface PosRepository {

    Observable<Boolean> register(PosRegister info);

    Observable<Boolean> save(PosInfo pos);

    Observable<PosInfo> getCached();

}
