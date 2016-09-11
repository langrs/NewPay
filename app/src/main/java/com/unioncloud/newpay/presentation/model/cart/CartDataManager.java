package com.unioncloud.newpay.presentation.model.cart;

import android.text.TextUtils;

import com.raizlabs.coreutils.events.Event;
import com.raizlabs.coreutils.events.IEvent;
import com.raizlabs.coreutils.functions.Delegate;
import com.raizlabs.coreutils.util.observable.lists.ObservableList;
import com.raizlabs.coreutils.util.observable.lists.ObservableListWrapper;
import com.unioncloud.newpay.domain.model.cart.AddProductEntry;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.presentation.presenter.cart.UpdateCartHandler;

import java.util.Iterator;
import java.util.List;


/**
 * Created by cwj on 16/7/1.
 */
public class CartDataManager {

    private static CartDataManager instance;

    public static CartDataManager getInstance() {
        if (instance == null) {
            instance = new CartDataManager();
        }
        return instance;
    }

    private ObservableListWrapper<CartItem> itemList = new ObservableListWrapper<>();
    private IEvent<CartDataManager> itemChanged = new Event<>();
    private UpdateCartHandler updateCartResponse;

    public void clear() {
        this.itemList.clear();
        notifyCartItemChanged();
    }

    public ObservableList<CartItem> getItemList() {
        return itemList;
    }

    public boolean hasItems() {
        return (itemList != null) && (itemList.size() > 0);
    }

    public void notifyCartItemChanged() {
        itemChanged.raiseEvent(this);
    }

    public void addCartItemListener(Delegate<CartDataManager> listener) {
        itemChanged.addListener(listener);
    }

    public void removeCartItemListener(Delegate<CartDataManager> listener) {
        itemChanged.removeListener(listener);
    }

    public boolean isEmpty() {
        return itemList.isEmpty();
    }

    // 在本地购物车中添加商品项
    public void addLocalItem(CartItem cartItem) {
        if (cartItem != null) {
            cartItem.setRowNumber(itemList.size() + 1);
            itemList.add(cartItem);
            notifyCartItemChanged();
        }
    }

    public void addLocalItems(List<CartItem> cartItems) {
        if (cartItems != null && cartItems.size() > 0) {
            for (CartItem cartItem: cartItems) {
                itemList.add(cartItem);
            }
            notifyCartItemChanged();
        }
    }

    // 修改本地购物车中的商品销售单价
    public void updateLocalItemUnitPrice(CartItem item, int newUnitPrice) {
        CartItem localItem = getLocalCartItem(item.getProductId(), item.getRowNumber());
        localItem.setSellUnitPrice(item.getPrice());
        notifyCartItemChanged();
    }

    // 修改本地购物车中的商品数量
    public void updateLocalItemQuantity(CartItem item, int quantity) {
        CartItem localItem = getLocalCartItem(item.getProductId(), item.getRowNumber());
        localItem.setQuantity(quantity);
        notifyCartItemChanged();
    }

    // 从服务器获取最新的购物车数据
    public UpdateCartHandler updateCartData() {
        if (this.updateCartResponse == null) {
            updateCartResponse = new UpdateCartHandler(this, true);
        }
        return updateCartResponse;
    }

    public CartItem getLocalCartItem(String productId, int rowNumber) {
        if (TextUtils.isEmpty(productId)) {
            return null;
        }
        for (CartItem cartItem : itemList) {
            if (productId.equals(cartItem.getProductId()) && (rowNumber == cartItem.getRowNumber())) {
                return cartItem;
            }
        }
        return null;
    }


    // 移除本地购物车中的商品项
    public void removeLocalItem(String productId, int rowNumber) {
        if (TextUtils.isEmpty(productId)) {
            return ;
        }
        Iterator<CartItem> itemIterator = itemList.iterator();
        while (itemIterator.hasNext()) {
            CartItem cartItem = itemIterator.next();
            if (productId.equals(cartItem.getProductId())
                    && (rowNumber == cartItem.getRowNumber())) {
                itemIterator.remove();
                break;
            }
        }
        updateRowNumber();
        notifyCartItemChanged();
    }

    private void updateRowNumber() {
        for(int i = 0, size = itemList.size(); i < size; i++) {
            CartItem cartItem = itemList.get(i);
            cartItem.setRowNumber(i+1);
        }
    }

    // 更新本地购物车中商品项的销售策略
    public void updateLocalSaleStrategy(List<CartItem> list) {
        for(CartItem strategiedItem : list) {
            CartItem cartItem = getLocalCartItem(strategiedItem.getProductId(), strategiedItem.getRowNumber());
            if (cartItem != null) {
                cartItem.updateSaleStrategy(strategiedItem);
            }
        }
        notifyCartItemChanged();
    }

}
