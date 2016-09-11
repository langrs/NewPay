package com.unioncloud.newpay.data.repository.checkout.datasource;

import com.unioncloud.newpay.data.entity.checkout.CheckoutItemEntity;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.erp.VipCard;
import com.unioncloud.newpay.domain.model.order.OrderType;
import com.unioncloud.newpay.domain.model.pos.PosInfo;

import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 16/8/15.
 */
public interface CheckoutStore {

    Observable<List<CheckoutItemEntity>> calculateAmounts(String orderId, PosInfo posInfo, VipCard
            vipCard, OrderType orderType, List<CartItem> itemList);
}
