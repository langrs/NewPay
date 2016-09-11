package com.unioncloud.newpay.data.repository.bankcardpay.datasource;

import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundRequest;
import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundResult;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleRequest;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleResult;

import rx.Observable;

/**
 * Created by cwj on 16/8/17.
 */
public interface BankcardPayStore {

    Observable<BankcardSaleResult> sale(BankcardSaleRequest request);

    Observable<BankcardRefundResult> saleVoid(BankcardRefundRequest request);

    Observable<BankcardRefundResult> refund(BankcardRefundRequest request);

}
