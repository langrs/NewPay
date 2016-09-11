package com.unioncloud.newpay.data.repository.vip;

import com.unioncloud.newpay.data.entity.vip.VipEntity;
import com.unioncloud.newpay.data.repository.StoreFactory;
import com.unioncloud.newpay.domain.model.erp.VipCard;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.repository.VipRepository;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by cwj on 16/8/17.
 */
public class VipDataRepository implements VipRepository {

    @Override
    public Observable<VipCard> queryByCd(String cdInfo, PosInfo posInfo) {
        return StoreFactory.getVipStore().queryByCd(cdInfo, posInfo)
                .map(new Func1<VipEntity, VipCard>() {
                    @Override
                    public VipCard call(VipEntity vipEntity) {
                        return mapper(vipEntity);
                    }
                });
    }

    @Override
    public Observable<VipCard> queryByBillNo(String billNo, PosInfo posInfo) {
        return StoreFactory.getVipStore().queryByBillNo(billNo, posInfo)
                .map(new Func1<VipEntity, VipCard>() {
                    @Override
                    public VipCard call(VipEntity vipEntity) {
                        return mapper(vipEntity);
                    }
                });
    }

    private VipCard mapper(VipEntity vipEntity) {
        if (vipEntity == null) {
            return null;
        }
        VipCard vipCard = new VipCard();
        vipCard.setCardNumber(vipEntity.getCkcode());
        vipCard.setCoBrandedNumber(vipEntity.getLmcode());
        vipCard.setPhoneNumber(vipEntity.getCust_mobill());
        vipCard.setName(vipEntity.getCust_name());
        vipCard.setSex(vipEntity.getCust_sex());
        vipCard.setAddress(vipEntity.getCust_addr());
        vipCard.setBirthday(vipEntity.getCust_birthday());
        vipCard.setIdNumber(vipEntity.getCust_id());
        vipCard.setPoints(vipEntity.getCardjf());
        vipCard.setCardType(vipEntity.getCardtype());
        vipCard.setCardStatus(vipEntity.getCardstatus());
        return vipCard;
    }
}
