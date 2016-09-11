package com.unioncloud.newpay.data.repository.gift;

import android.content.Intent;

import com.unioncloud.newpay.data.entity.gift.GiftCardEntity;
import com.unioncloud.newpay.data.repository.StoreFactory;
import com.unioncloud.newpay.domain.model.erp.GiftCard;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.repository.GiftRepository;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by cwj on 16/9/3.
 */
public class GiftDataRepositroy implements GiftRepository {

    @Override
    public Observable<GiftCard> queryGiftCard(String track, PosInfo posInfo) {
        return StoreFactory.getGiftStore().queryGiftCard(track, posInfo)
                .map(new Func1<GiftCardEntity, GiftCard>() {
                    @Override
                    public GiftCard call(GiftCardEntity giftCardEntity) {
                        return mapper(giftCardEntity);
                    }
                });
    }

    private static GiftCard mapper(GiftCardEntity entity) {
        if (entity == null) {
            return null;
        }
        GiftCard giftCard = new GiftCard();
        giftCard.setCardNumber(entity.getCkcode());
        giftCard.setAmountValue(entity.getCardvalue());
        giftCard.setCardStatus(entity.getCardstatus());
        giftCard.setCardType(entity.getCardtype());
        return giftCard;
    }
}
