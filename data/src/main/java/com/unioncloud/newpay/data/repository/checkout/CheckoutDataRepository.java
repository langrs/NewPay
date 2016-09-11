package com.unioncloud.newpay.data.repository.checkout;

import com.unioncloud.newpay.data.entity.checkout.CheckoutItemEntity;
import com.unioncloud.newpay.data.repository.StoreFactory;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.erp.VipCard;
import com.unioncloud.newpay.domain.model.order.OrderType;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.repository.CheckoutRepository;
import com.unioncloud.newpay.domain.utils.MoneyUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by cwj on 16/8/15.
 */
public class CheckoutDataRepository implements CheckoutRepository {

    @Override
    public Observable<List<CartItem>> calculateAmounts(String orderId, PosInfo posInfo, VipCard
            vipCard, OrderType orderType, List<CartItem> itemList) {
        return StoreFactory.getCheckoutStore().calculateAmounts(orderId, posInfo, vipCard, orderType, itemList)
                .map(new Func1<List<CheckoutItemEntity>, List<CartItem>>() {
                    @Override
                    public List<CartItem> call(List<CheckoutItemEntity> checkoutItemEntities) {
                        return mapperList(checkoutItemEntities);
                    }
                });
    }

    private static List<CartItem> mapperList(List<CheckoutItemEntity> entityList) {
        ArrayList<CartItem> list = new ArrayList<>();
        for (CheckoutItemEntity entity : entityList) {
            list.add(mapper(entity));
        }
        return list;
    }

    private static CartItem mapper(CheckoutItemEntity entity) {
        if (entity != null) {
            CartItem cartItem = new CartItem();
            cartItem.setRowNumber(Integer.valueOf(entity.getRowNo()));
            cartItem.setStoreId(entity.getStoreId());
            cartItem.setProductId(entity.getItemId());
            cartItem.setSellUnitPrice(MoneyUtils.getFen(entity.getSalePrice()));
            cartItem.setSellUnitPriceReal(MoneyUtils.getFen(entity.getSalePriceReal()));
            cartItem.setDiscountAmount(MoneyUtils.getFen(entity.getAllDistAmt()));
            cartItem.setSaleAmount(MoneyUtils.getFen(entity.getItemSaleAmt()));
            cartItem.setPromInfo(entity.getPromInfo());
            cartItem.setPromDiscount(Integer.valueOf(entity.getPromDisc()));
            cartItem.setPromDiscountAmount(MoneyUtils.getFen(entity.getPromDiscAmt()));
            cartItem.setVipDiscount(Integer.valueOf(entity.getVipDiscAmt()));
            cartItem.setVipDiscountAmount(MoneyUtils.getFen(entity.getVipDiscAmt()));
            cartItem.setReduceAmount(MoneyUtils.getFen(entity.getReduceAmt()));
            cartItem.setPoints(entity.getPoints());
            cartItem.setCouponDiscountAmount(MoneyUtils.getFen(entity.getCouponDisc()));
            return cartItem;
        }
        return null;
    }
}
