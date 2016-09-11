package com.unioncloud.newpay.data.entity.product;

import com.unioncloud.newpay.data.entity.ResultEntity;

/**
 * Created by cwj on 16/8/8.
 */
public class QueryProductResultEntity extends ResultEntity {

    ProductEntity entity;

    public ProductEntity getEntity() {
        return entity;
    }

    public void setEntity(ProductEntity entity) {
        this.entity = entity;
    }
}
