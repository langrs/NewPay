package com.unioncloud.newpay.domain.interactor.product;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.product.Product;
import com.unioncloud.newpay.domain.repository.ProductRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 16/6/29.
 */
public class QueryProductInteractor extends BaseInteractor<List<Product>> {

    private String productNumber;
    private PosInfo posInfo;
    private ProductRepository repository;

    public QueryProductInteractor(ExecutorProvider provider,
                                  String productNumber,
                                  PosInfo posInfo,
                                  ProductRepository repository) {
        super(provider);
        this.productNumber = productNumber;
        this.posInfo = posInfo;
        this.repository = repository;
    }

    public QueryProductInteractor(ExecutorProvider provider,
                                  PosInfo posInfo,
                                  ProductRepository repository) {
        super(provider);
        this.posInfo = posInfo;
        this.repository = repository;
    }

    @Override
    protected Observable<List<Product>> bindObservable() {
        if (productNumber != null) {
            return repository.queryByNumber(productNumber, posInfo)
                    .toList();
        } else {
            return repository.queryByStoreId(posInfo);
        }
    }
}
