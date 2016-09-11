package com.unioncloud.newpay.data.repository.product;

import com.unioncloud.newpay.data.entity.product.ProductEntity;
import com.unioncloud.newpay.data.repository.StoreFactory;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.product.Product;
import com.unioncloud.newpay.domain.repository.ProductRepository;
import com.unioncloud.newpay.domain.utils.MoneyUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by cwj on 16/8/8.
 */
public class ProductDataRepository implements ProductRepository {

    @Override
    public Observable<Product> queryByNumber(String productId, PosInfo posInfo) {
        return StoreFactory.getProductStore().queryByNumber(productId, posInfo)
                .map(new Func1<ProductEntity, Product>() {
                    @Override
                    public Product call(ProductEntity productEntity) {
                        return mapper(productEntity);
                    }
                });
    }

    @Override
    public Observable<List<Product>> queryByStoreId(PosInfo posInfo) {
        return StoreFactory.getProductStore().queryByPos(posInfo)
                .map(new Func1<List<ProductEntity>, List<Product>>() {
                    @Override
                    public List<Product> call(List<ProductEntity> productEntities) {
                        return mapperList(productEntities);
                    }
                });
    }

    private Product mapper(ProductEntity entity) {
        if (entity != null) {
            Product product = new Product();
            product.setCompanyId(entity.getCompanyId());
            product.setShopId(entity.getShopId());
            product.setStoreId(entity.getStoreId());
            product.setProductId(entity.getItemId());
            product.setProductNumber(entity.getItemNo());
            product.setProductName(entity.getItemName());
            product.setInnerNumber(entity.getItemSubno());
            product.setPrice(MoneyUtils.getFen(entity.getSalePrice()));
            product.setIsPrice(entity.getIsPrice());
            product.setAttr(entity.getItemAttr());
            product.setBrandId(entity.getBrandId());
            product.setCategoryId(entity.getCategoryId());
            product.setIsFlag(entity.getIsFlag());
            return product;
        }
        return null;
    }

    private List<Product> mapperList(List<ProductEntity> entityList) {
        if (entityList != null) {
            ArrayList<Product> list = new ArrayList<>(entityList.size());
            for (ProductEntity entity : entityList) {
                list.add(mapper(entity));
            }
            return list;
        }
        return null;
    }
}
