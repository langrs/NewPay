package com.unioncloud.newpay.domain.repository;

import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.product.Product;

import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 16/4/18.
 */
public interface ProductRepository {

    Observable<Product> queryByNumber(String productNumber, PosInfo posInfo);

    Observable<List<Product>> queryByStoreId(PosInfo posInfo);

}
