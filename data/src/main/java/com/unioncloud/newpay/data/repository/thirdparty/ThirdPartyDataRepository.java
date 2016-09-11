package com.unioncloud.newpay.data.repository.thirdparty;

import com.unioncloud.newpay.data.repository.StoreFactory;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyOrder;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyRefundOrder;
import com.unioncloud.newpay.domain.model.thirdparty.ThirdPartyRefundRecord;
import com.unioncloud.newpay.domain.repository.ThirdPartyRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/18.
 */
public class ThirdPartyDataRepository implements ThirdPartyRepository {

    @Override
    public Observable<ThirdPartyOrder> pay(String orderId, String code,
                                           String attach, int total,
                                           String ip,
                                           String body) {
        return StoreFactory.getThirdPartyStore().pay(orderId, code, attach, total, ip, body);
    }

    @Override
    public Observable<ThirdPartyOrder> queryOrder(String orderId, String transactionId) {
        return StoreFactory.getThirdPartyStore().queryOrder(orderId, transactionId);
    }

    @Override
    public Observable<ThirdPartyRefundRecord> queryRefund(String orderId, String refundOrderId,
                                                          String transactionId, String refundId) {
        return StoreFactory.getThirdPartyStore().queryRefund(orderId, refundOrderId, transactionId, refundId);
    }

    @Override
    public Observable<ThirdPartyRefundOrder> refund(String orderId, String transactionId,
                                                    int totalFee, String refundOrderId,
                                                    int refundFee) {
        return StoreFactory.getThirdPartyStore().refund(orderId,
                transactionId, totalFee, refundOrderId, refundFee);
    }
}
