package com.unioncloud.newpay.presentation.presenter.login;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.domain.interactor.product.QueryProductInteractor;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.product.Product;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;

import java.util.List;

import rx.Subscriber;

/**
 * Created by cwj on 16/8/14.
 */
public class QueryProductHandler extends UpdateHandler<PosDataManager, QueryProductHandler>
        implements Runnable {

    private PosInfo posInfo;

    public QueryProductHandler(PosDataManager data, PosInfo posInfo) {
        super(data, true);
        this.posInfo = posInfo;
    }

    @Override
    public void run() {
        QueryProductInteractor interactor = new QueryProductInteractor(
                PresenterUtils.getExecutorProvider(),
                posInfo,
                PresenterUtils.getProductRepository());
        interactor.execute(new Subscriber<List<Product>>() {
            @Override
            public void onCompleted() {
                unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                onUpdateFailed();
            }

            @Override
            public void onNext(List<Product> products) {
                getData().onLoadProducts(products);
                onUpdateCompleted();
            }
        });
    }
}
