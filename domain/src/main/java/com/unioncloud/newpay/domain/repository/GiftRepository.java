package com.unioncloud.newpay.domain.repository;

import com.unioncloud.newpay.domain.model.erp.GiftCard;
import com.unioncloud.newpay.domain.model.pos.PosInfo;

import rx.Observable;

/**
 * Created by cwj on 16/9/3.
 */
public interface GiftRepository {

    Observable<GiftCard> queryGiftCard(String track, PosInfo posInfo);

}
