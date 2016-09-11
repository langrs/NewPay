package com.unioncloud.newpay.data.repository.pos.datasource;

import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.pos.PosRegister;

import rx.Observable;

/**
 * Created by cwj on 16/8/12.
 */
public interface PosStore {

    Observable<Boolean> register(PosRegister info);

    Observable<Boolean> save(PosInfo pos);

    Observable<PosInfo> getCached();

}
