package com.unioncloud.newpay.data.repository.gift.datasource;

import com.unioncloud.newpay.data.entity.gift.GiftCardEntity;
import com.unioncloud.newpay.domain.model.pos.PosInfo;

import rx.Observable;

/**
 * Created by cwj on 16/9/3.
 */
public interface GiftStore {

    Observable<GiftCardEntity> queryGiftCard(String track, PosInfo posInfo);

}
