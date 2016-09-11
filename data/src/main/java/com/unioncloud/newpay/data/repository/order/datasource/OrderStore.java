package com.unioncloud.newpay.data.repository.order.datasource;

import com.unioncloud.newpay.data.entity.order.SaleOrderEntity;
import com.unioncloud.newpay.data.entity.order.SaleResultEntity;
import com.unioncloud.newpay.domain.model.order.QuerySaleOrderCommand;
import com.unioncloud.newpay.domain.model.order.SaleOrder;
import com.unioncloud.newpay.domain.model.pos.PosInfo;

import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 16/8/16.
 */
public interface OrderStore {

    Observable<String> getSerialNumber(PosInfo posInfo);

    Observable<SaleResultEntity> submitSale(SaleOrder saleOrder);

    Observable<List<SaleOrderEntity>> querySaleOrder(QuerySaleOrderCommand command);
}
