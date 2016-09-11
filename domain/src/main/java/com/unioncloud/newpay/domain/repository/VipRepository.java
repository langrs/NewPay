package com.unioncloud.newpay.domain.repository;

import com.unioncloud.newpay.domain.model.erp.VipCard;
import com.unioncloud.newpay.domain.model.pos.PosInfo;

import rx.Observable;

/**
 * Created by cwj on 16/8/8.
 */
public interface VipRepository {

    Observable<VipCard> queryByCd(String cdInfo, PosInfo posInfo);

    Observable<VipCard> queryByBillNo(String billNo, PosInfo posInfo);

}
