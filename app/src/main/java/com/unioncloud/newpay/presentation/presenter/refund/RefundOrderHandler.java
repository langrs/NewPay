package com.unioncloud.newpay.presentation.presenter.refund;

import android.text.TextUtils;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.order.SaleOrderInteractor;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.erp.VipCard;
import com.unioncloud.newpay.domain.model.order.OrderType;
import com.unioncloud.newpay.domain.model.order.SaleOrder;
import com.unioncloud.newpay.domain.model.order.SaleOrderHeader;
import com.unioncloud.newpay.domain.model.order.SaleOrderResult;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.product.Product;
import com.unioncloud.newpay.domain.utils.DateFormatUtils;
import com.unioncloud.newpay.presentation.model.cart.CartDataManager;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.model.refund.RefundDataManager;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/26.
 */
public class RefundOrderHandler extends UpdateHandler<SaleOrderResult, RefundOrderHandler>
        implements Runnable {

    PosInfo posInfo;
    String orderId;
    SaleOrder originalOrder;
    List<PaymentUsed> refundPaidInfos;

    public RefundOrderHandler(PosInfo posInfo, String refundOrderId,
                              SaleOrder originalOrder,
                              List<PaymentUsed> refundPaidInfos) {
        super(null, true);
        this.posInfo = posInfo;
        this.orderId = refundOrderId;
        this.originalOrder = originalOrder;
        this.refundPaidInfos = refundPaidInfos;
    }

    @Override
    public void run() {
        SaleOrderInteractor interactor = new SaleOrderInteractor(
                PresenterUtils.getExecutorProvider(),
                createRefundOrder(),
                PresenterUtils.getSaleOrderRepository());
        interactor.execute(new Subscriber<SaleOrderResult>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(SaleOrderResult saleOrderResult) {
                data = saleOrderResult;
                onUpdateCompleted();
            }
        });
    }

    private SaleOrder createRefundOrder() {
        SaleOrderHeader header = new SaleOrderHeader();
//        原单流水
        header.setOriginalSaleNo(originalOrder.getHeader().getSaleNumber());
        header.setCompanyId(posInfo.getCompanyId());
        header.setShopId(posInfo.getShopId());
        header.setStoreId(posInfo.getStoreId());
        header.setPosId(posInfo.getPosId());
        header.setUserId(posInfo.getUserId());
        header.setSaleNumber(orderId);
        header.setSaleDate(DateFormatUtils.yyyyMMddHHmmss(new Date()));
        header.setDealType(OrderType.REFUND.getValue());
        header.setSaleType("01");
        header.setVipCardNumber(originalOrder.getHeader().getVipCardNumber());
        header.setPreviousPoints(originalOrder.getHeader().getPreviousPoints());
        header.setSalePoints("-" + originalOrder.getHeader().getSalePoints());
        header.setOriginalAmount("-" + originalOrder.getHeader().getOriginalAmount());
        header.setSaleAmount("-" + originalOrder.getHeader().getSaleAmount());

        header.setPayAmount("-" + originalOrder.getHeader().getPayAmount());
        if (!TextUtils.isEmpty(originalOrder.getHeader().getDiscountAmount())) {
            header.setDiscountAmount("-" + originalOrder.getHeader().getDiscountAmount());
        }
        if (!TextUtils.isEmpty(originalOrder.getHeader().getVipDiscountAmount())) {
            header.setVipDiscountAmount("-" + originalOrder.getHeader().getVipDiscountAmount());
        }
        header.setIsTrain("");
        header.setRefundAmount("0");
        header.setEbillType("01");
//        退货授权人
        header.setSalesId(RefundDataManager.getInstance().getAuthorizer());

//        header.setChangedAmount(MoneyUtils.fenToString(changedAmount));
//        header.setExcessAmount(MoneyUtils.fenToString(excessAmount))

        SaleOrder refundOrder = new SaleOrder();
        refundOrder.setHeader(header);

        ArrayList<CartItem> cartItems = new ArrayList<>();
        for (CartItem item : originalOrder.getItemList()) {
            cartItems.add(convertProductItem(item));
        }
        refundOrder.setItemList(cartItems);
        refundOrder.setPaymentUsedList(refundPaidInfos);

        RefundDataManager.getInstance().addLocalItems(cartItems);

        return refundOrder;
    }

    // 将原销售单的商品项转换成退货单中的商品项
    private CartItem convertProductItem(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }
        Product product = PosDataManager.getInstance().getLocalProductById(cartItem.getProductId());

        CartItem refundItem = new CartItem();
        refundItem.setProductId(cartItem.getProductId());
        refundItem.setProductNumber(product.getProductNumber());
        refundItem.setProductName(product.getProductName());
        refundItem.setRowNumber(cartItem.getRowNumber());
        refundItem.setQuantity(-cartItem.getQuantity());
        refundItem.setSellUnitPrice(cartItem.getSellUnitPrice());           // 原单价
//        refundItem.setSellUnitPriceReal(cartItem.getSellUnitPriceReal());   // 折后单价 原订单中没有
        refundItem.setSaleAmount(-cartItem.getSaleAmount());                // 折后总额
        refundItem.setDiscountAmount(-cartItem.getDiscountAmount());        // 折扣总额
        if (!TextUtils.isEmpty(cartItem.getPoints())) {
            refundItem.setPoints("-" + cartItem.getPoints());
        }
//        lzm增加退货的换倍字段
        refundItem.setVipDiscount(cartItem.getVipDiscount());
        refundItem.setVipDiscountAmount(-cartItem.getVipDiscountAmount());
        refundItem.setPromDiscount(cartItem.getPromDiscount());
        refundItem.setPromDiscountAmount(-cartItem.getPromDiscountAmount());
        return refundItem;
    }

}
