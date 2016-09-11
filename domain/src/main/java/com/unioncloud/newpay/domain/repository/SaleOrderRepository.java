package com.unioncloud.newpay.domain.repository;

import com.unioncloud.newpay.domain.model.order.QuerySaleOrderCommand;
import com.unioncloud.newpay.domain.model.order.SaleOrder;
import com.unioncloud.newpay.domain.model.order.SaleOrderResult;
import com.unioncloud.newpay.domain.model.pos.PosInfo;

import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 16/8/8.
 */
public interface SaleOrderRepository {

    Observable<String> getSerialNumber(PosInfo posInfo);

    Observable<SaleOrderResult> submitSale(SaleOrder saleOrder);

    Observable<List<SaleOrder>> querySaleOrder(QuerySaleOrderCommand command);
}
