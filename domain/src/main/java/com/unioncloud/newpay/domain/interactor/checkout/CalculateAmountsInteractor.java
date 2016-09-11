package com.unioncloud.newpay.domain.interactor.checkout;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.erp.VipCard;
import com.unioncloud.newpay.domain.model.order.OrderType;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.repository.CheckoutRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 16/8/11.
 */
public class CalculateAmountsInteractor extends BaseInteractor<List<CartItem>> {

    private CheckoutRepository repository;
    private String orderId;
    private PosInfo posInfo;
    private VipCard vipCard;
    private OrderType orderType;
    private List<CartItem> cartItems;

    public CalculateAmountsInteractor(ExecutorProvider provider,
                                      String orderId,
                                      PosInfo posInfo,
                                      VipCard vipCard,
                                      OrderType orderType,
                                      List<CartItem> cartItems,
                                      CheckoutRepository repository) {
        super(provider);
        this.orderId = orderId;
        this.posInfo = posInfo;
        this.vipCard = vipCard;
        this.orderType = orderType;
        this.cartItems = cartItems;
        this.repository = repository;
    }

    @Override
    protected Observable<List<CartItem>> bindObservable() {
        return repository.calculateAmounts(orderId, posInfo, vipCard, orderType, cartItems);
    }
}
