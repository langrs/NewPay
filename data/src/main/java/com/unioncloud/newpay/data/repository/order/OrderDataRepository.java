package com.unioncloud.newpay.data.repository.order;

import android.text.TextUtils;

import com.unioncloud.newpay.data.entity.coupon.CouponEntity;
import com.unioncloud.newpay.data.entity.order.SaleHeadEntity;
import com.unioncloud.newpay.data.entity.order.SaleItemEntity;
import com.unioncloud.newpay.data.entity.order.SaleOrderEntity;
import com.unioncloud.newpay.data.entity.order.SalePayEntity;
import com.unioncloud.newpay.data.entity.order.SaleResultEntity;
import com.unioncloud.newpay.data.repository.StoreFactory;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.domain.model.order.QuerySaleOrderCommand;
import com.unioncloud.newpay.domain.model.order.SaleOrder;
import com.unioncloud.newpay.domain.model.order.SaleOrderHeader;
import com.unioncloud.newpay.domain.model.order.SaleOrderResult;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.repository.SaleOrderRepository;
import com.unioncloud.newpay.domain.utils.MoneyUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by cwj on 16/8/15.
 */
public class OrderDataRepository implements SaleOrderRepository {

    @Override
    public Observable<String> getSerialNumber(PosInfo posInfo) {
        return Observable.just("0100");
    }

    @Override
    public Observable<SaleOrderResult> submitSale(SaleOrder saleOrder) {
        return StoreFactory.getOrderStore().submitSale(saleOrder)
                .map(new Func1<SaleResultEntity, SaleOrderResult>() {
                    @Override
                    public SaleOrderResult call(SaleResultEntity saleResultEntity) {
                        return mapperResult(saleResultEntity);
                    }
                });
    }

    private static SaleOrderResult mapperResult(SaleResultEntity resultEntity) {
        if (resultEntity == null) {
            return null;
        }
        SaleOrderResult result = new SaleOrderResult();
        result.setSaleNo(resultEntity.getHead().getSaleNo());
        result.setSalePoints(resultEntity.getHead().getSalePoints());
        result.setSaleDate(resultEntity.getHead().getSaleDate());
        result.setOriginalPoints(resultEntity.getHead().getOriginalPoints());

        ArrayList<Coupon> coupons = new ArrayList<>();
        if (resultEntity.getCoupons() != null) {
            for (CouponEntity couponEntity : resultEntity.getCoupons()) {
                Coupon coupon = mapperCoupon(couponEntity);
                if (coupon != null) {
                    coupons.add(coupon);
                }
            }
        }
        result.setCouponList(coupons);

        return result;
    }

    private static Coupon mapperCoupon(CouponEntity couponEntity) {
        if (couponEntity != null) {
            Coupon coupon = new Coupon();
            coupon.setCouponNo(couponEntity.getCouponNo());
            coupon.setCouponValue(couponEntity.getCouponValue());
            coupon.setState(couponEntity.getState());
            coupon.setEndDate(couponEntity.getEndDate());
            return coupon;
        }
        return null;
    }

    @Override
    public Observable<List<SaleOrder>> querySaleOrder(QuerySaleOrderCommand command) {
        return StoreFactory.getOrderStore().querySaleOrder(command)
                .map(new Func1<List<SaleOrderEntity>, List<SaleOrder>>() {
                    @Override
                    public List<SaleOrder> call(List<SaleOrderEntity> list) {
                        ArrayList<SaleOrder> orders = new ArrayList<SaleOrder>();
                        if (list != null) {
                            for (SaleOrderEntity entity : list) {
                                orders.add(mapperOrder(entity));
                            }
                        }
                        return orders;
                    }
                });
    }

    private static SaleOrder mapperOrder(SaleOrderEntity entity) {
        if (entity == null) {
            return null;
        }
        SaleOrder order = new SaleOrder();

        SaleOrderHeader header = mapperOrderHeader(entity.getSaleHead());
        order.setHeader(header);

        ArrayList<CartItem> itemList = new ArrayList<>();
        if (entity.getSaleDetail() != null) {
            for (SaleItemEntity itemEntity: entity.getSaleDetail()) {
                itemList.add(mapperProductItem(itemEntity));
            }
        }
        order.setItemList(itemList);

        ArrayList<PaymentUsed> paidList = new ArrayList<>();
        if (entity.getSalePay() != null) {
            for (SalePayEntity payEntity : entity.getSalePay()) {
                paidList.add(mapperPaidInfo(payEntity));
            }
        }
        order.setPaymentUsedList(paidList);

        return order;
    }

    private static SaleOrderHeader mapperOrderHeader(SaleHeadEntity entity) {
        if (entity == null) {
            return null;
        }
        SaleOrderHeader header = new SaleOrderHeader();
        header.setCompanyId(entity.getCompanyId());
        header.setShopId(entity.getShopId());
        header.setStoreId(entity.getStoreId());
        header.setPosId(entity.getPosId());
        header.setSaleNumber(entity.getSaleNo());
        header.setSaleDate(entity.getSaleDate());
        header.setUserId(entity.getUserId());
        header.setDealType(entity.getDealType());
        header.setSaleType(entity.getSaleType());
        header.setVipCardNumber(entity.getVipNo());
        header.setPreviousPoints(entity.getOriginalPoints());
        header.setSalePoints(entity.getSalePoints());
        header.setOriginalAmount(entity.getOriginalAmt());
        header.setSaleAmount(entity.getSaleAmt());
        header.setPayAmount(entity.getPayAmt());
        header.setDiscountAmount(entity.getDicAmt());
        header.setVipDiscountAmount(entity.getVipDicAmt());
        header.setChangedAmount(entity.getChangeAmt());
        header.setExcessAmount(entity.getExcessAmt());
        header.setIsTrain(entity.getIsTrain());
        header.setReason(entity.getReason());
        header.setRefundAmount(entity.getRetAmt());
        header.setEbillType(entity.getEbillType());
        header.setUpFlag(entity.getUpFlag());
        header.setUpData(entity.getUpData());
        return header;
    }

    private static CartItem mapperProductItem(SaleItemEntity entity) {
        if (entity == null) {
            return null;
        }
        CartItem item = new CartItem();
        item.setProductId(entity.getItemId());
        item.setProductNumber(entity.getItemNo());
        if (!TextUtils.isEmpty(entity.getRowNo())) {
            item.setRowNumber(Integer.valueOf(entity.getRowNo()));
        }
        if (!TextUtils.isEmpty(entity.getSaleNum())) {
            item.setQuantity(Integer.valueOf(entity.getSaleNum()));
        }
        if (!TextUtils.isEmpty(entity.getSalePrice())) {
            item.setSellUnitPrice(MoneyUtils.getFen(entity.getSalePrice()));
        }
        if (!TextUtils.isEmpty(entity.getAllDistAmt())) {
            item.setDiscountAmount(MoneyUtils.getFen(entity.getAllDistAmt()));
        }
        if (!TextUtils.isEmpty(entity.getItemSaleAmt())) {
            item.setSaleAmount(MoneyUtils.getFen(entity.getItemSaleAmt()));
        }
        item.setPoints(entity.getSalePoints());
        if (!TextUtils.isEmpty(entity.getVipDisc())) {
            item.setVipDiscount(Integer.valueOf(entity.getVipDisc()));
        }
        if (!TextUtils.isEmpty(entity.getVipDiscAmt())) {
            item.setVipDiscountAmount(MoneyUtils.getFen(entity.getVipDiscAmt()));
        }
        return item;
    }

    private static PaymentUsed mapperPaidInfo(SalePayEntity entity) {
        if (entity == null) {
            return null;
        }
        PaymentUsed used = new PaymentUsed();
        if (!TextUtils.isEmpty(entity.getRowNo())) {
            used.setRowNo(Integer.valueOf(entity.getRowNo()));
        }
        used.setRelationNumber(entity.getBillNo());
        used.setPaymentId(entity.getPaymodeId());
        if (!TextUtils.isEmpty(entity.getExcessAmt())) {
            used.setExcessAmount(MoneyUtils.getFen(entity.getExcessAmt()));
        }
        if (!TextUtils.isEmpty(entity.getPayAmt())) {
            used.setPayAmount(MoneyUtils.getFen(entity.getPayAmt()));
        }
        if (!TextUtils.isEmpty(entity.getChangeAmt())) {
            used.setChangeAmount(MoneyUtils.getFen(entity.getChangeAmt()));
        }
        used.setCurrencyId(entity.getCurrencyId());
        used.setExchangeRate(entity.getExchangeRate());
        return used;
    }
}
