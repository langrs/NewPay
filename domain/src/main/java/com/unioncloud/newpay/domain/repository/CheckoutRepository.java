package com.unioncloud.newpay.domain.repository;

import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.erp.VipCard;
import com.unioncloud.newpay.domain.model.order.OrderType;
import com.unioncloud.newpay.domain.model.pos.PosInfo;

import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 16/8/11.
 */
public interface CheckoutRepository {

    /**
     * 计算金额
     * @param orderId
     * @param posInfo
     * @param vipCard
     * @return
     */
    Observable<List<CartItem>> calculateAmounts(String orderId, PosInfo posInfo,
                                                VipCard vipCard, OrderType orderType,
                                                List<CartItem> itemList);
}
