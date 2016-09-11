package com.unioncloud.newpay.data.repository.product.datasource;

import com.unioncloud.newpay.data.entity.product.ProductEntity;
import com.unioncloud.newpay.domain.model.pos.PosInfo;

import java.util.List;

import rx.Observable;

/**
 * Created by cwj on 16/8/8.
 */
public interface ProductStore {

    Observable<ProductEntity> queryByNumber(String productNumber, PosInfo info);


    Observable<List<ProductEntity>> queryByPos(PosInfo posInfo);

}
