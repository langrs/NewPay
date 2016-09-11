package com.unioncloud.newpay.data.repository.vip.datasource;

import com.unioncloud.newpay.data.entity.vip.VipEntity;
import com.unioncloud.newpay.domain.model.pos.PosInfo;

import rx.Observable;

/**
 * Created by cwj on 16/8/17.
 */
public interface VipStore {

    Observable<VipEntity> queryByCd(String cdInfo, PosInfo posInfo);

    Observable<VipEntity> queryByBillNo(String billNo, PosInfo posInfo);

}
