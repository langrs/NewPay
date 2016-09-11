package com.unioncloud.newpay.domain.repository;

import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundRequest;
import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundResult;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleRequest;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleResult;

import rx.Observable;

/**
 * Created by cwj on 16/8/17.
 */
public interface BankcardTradeRepository {

    /** 消费 */
    Observable<BankcardSaleResult> sale(BankcardSaleRequest request);

    /** 撤销(当天退款) */
    Observable<BankcardRefundResult> saleVoid(BankcardRefundRequest request);

    /** 退货 */
    Observable<BankcardRefundResult> refund(BankcardRefundRequest request);

}
