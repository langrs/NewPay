package com.unioncloud.newpay.presentation.presenter.cart;

import com.unioncloud.newpay.domain.interactor.cart.RemoveItemsInteractor;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.presentation.presenter.BooleanUpdateHandler;
import com.unioncloud.newpay.presentation.model.cart.CartDataManager;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/9.
 */
public class RemoveCartItemsHandler extends BooleanUpdateHandler
        implements Runnable {

    private CartItem cartItem;

    public RemoveCartItemsHandler(CartItem cartItem) {
        super(true);
        this.cartItem = cartItem;
    }

    private static List<CartItem> mapList(CartItem cartItem) {
        ArrayList<CartItem> localArrayList = new ArrayList<>();
        localArrayList.add(cartItem);
        return localArrayList;
    }

    @Override
    public void run() {
        RemoveItemsInteractor interactor = new RemoveItemsInteractor(
                PresenterUtils.getExecutorProvider(),
                cartItem,
                PresenterUtils.getCartDataRepository()
        );
        interactor.execute(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }
            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }
            @Override
            public void onNext(Boolean aBoolean) {
                data = aBoolean;
                CartDataManager.getInstance().removeLocalItem(
                        cartItem.getProductId(), cartItem.getRowNumber());
                onUpdateCompleted();
            }
        });
    }

    @Override
    public boolean isSuccess() {
        return super.isSuccess() && getData();
    }
}
