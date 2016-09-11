package com.unioncloud.newpay.data.repository.bankcardpay;

import android.content.Context;

import com.unioncloud.newpay.data.repository.StoreFactory;
import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundRequest;
import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundResult;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleRequest;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleResult;
import com.unioncloud.newpay.domain.repository.BankcardTradeRepository;

import rx.Observable;

/**
 * Created by cwj on 16/8/17.
 */
public class BankcardPayDataRepository implements BankcardTradeRepository {

    private Context context;

    public BankcardPayDataRepository(Context context) {
        this.context = context;
    }

    @Override
    public Observable<BankcardSaleResult> sale(BankcardSaleRequest request) {
        return StoreFactory.getBankcardPayStore(context).sale(request);
    }

    @Override
    public Observable<BankcardRefundResult> saleVoid(BankcardRefundRequest request) {
        return StoreFactory.getBankcardPayStore(context).saleVoid(request);
    }

    @Override
    public Observable<BankcardRefundResult> refund(BankcardRefundRequest request) {
        return StoreFactory.getBankcardPayStore(context).refund(request);
    }

}
