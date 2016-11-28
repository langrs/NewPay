package com.unioncloud.newpay.data.repository.print.datasource;

import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.domain.model.order.OrderStatistics;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.print.PrintOrderInfo;
import com.unioncloud.newpay.domain.model.print.PrintThirdPartyOrder;

import rx.Observable;

/**
 * Created by cwj on 16/8/19.
 */
public interface PrintStore {

    Observable<Boolean> printOrder(PrintOrderInfo orderInfo);

    Observable<Boolean> printCoupon(Coupon coupon);

    Observable<Boolean> printOrderStatistics(OrderStatistics statistics, PosInfo posInfo);

    Observable<Boolean> printThirdPay(PrintThirdPartyOrder order);
}
