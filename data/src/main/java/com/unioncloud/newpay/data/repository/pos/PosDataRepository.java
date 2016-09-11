package com.unioncloud.newpay.data.repository.pos;

import com.unioncloud.newpay.data.repository.StoreFactory;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.pos.PosRegister;
import com.unioncloud.newpay.domain.repository.PosRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/12.
 */
public class PosDataRepository implements PosRepository {

    @Override
    public Observable<Boolean> register(PosRegister info) {
        return StoreFactory.getPosStore().register(info);
    }

    @Override
    public Observable<Boolean> save(PosInfo pos) {
        return null;
    }

    @Override
    public Observable<PosInfo> getCached() {
        return null;
    }
}
