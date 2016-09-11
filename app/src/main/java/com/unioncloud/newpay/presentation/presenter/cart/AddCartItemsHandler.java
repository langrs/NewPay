package com.unioncloud.newpay.presentation.presenter.cart;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.cart.AddItemIteractor;
import com.unioncloud.newpay.domain.model.cart.AddProductEntry;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.cart.CartMapper;
import com.unioncloud.newpay.presentation.model.cart.CartDataManager;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/14.
 */
public class AddCartItemsHandler extends UpdateHandler<Boolean, AddCartItemsHandler>
        implements Runnable {

    private AddProductEntry entry;

    public AddCartItemsHandler(AddProductEntry entry) {
        super(Boolean.FALSE, true);
        this.entry = entry;
    }

    @Override
    public void run() {
        AddItemIteractor itemIteractor = new AddItemIteractor(
                PresenterUtils.getExecutorProvider(),
                entry,
                PresenterUtils.getCartDataRepository());
        itemIteractor.execute(new Subscriber<Boolean>() {
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
                CartItem cartItem = CartMapper.toFrom(entry);
                CartDataManager.getInstance().addLocalItem(cartItem);
                onUpdateCompleted();
            }
        });
    }
}
