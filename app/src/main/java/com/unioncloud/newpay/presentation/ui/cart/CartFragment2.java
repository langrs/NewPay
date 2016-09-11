package com.unioncloud.newpay.presentation.ui.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.RetrievedUpdateHandler;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.raizlabs.coreutils.functions.Delegate;
import com.raizlabs.coreutils.threading.ThreadingUtils;
import com.raizlabs.coreutils.view.ViewUtils;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.order.OrderType;
import com.unioncloud.newpay.presentation.model.cart.CartDataManager;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.presenter.BooleanUpdateHandler;
import com.unioncloud.newpay.presentation.presenter.cart.RemoveCartItemsHandler;
import com.unioncloud.newpay.presentation.presenter.cart.UpdateCartHandler;
import com.unioncloud.newpay.presentation.presenter.cart.UpdateItemQuantityHandler;
import com.unioncloud.newpay.presentation.ui.cart.views.CartItemAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by cwj on 16/8/17.
 */
public class CartFragment2 extends StatedFragment implements CartItemMenuListener {


    public static CartFragment2 getInstance() {
        CartFragment2 fragment = new CartFragment2();
        return fragment;
    }

    private static StateUpdateHandlerListener<CartFragment2, UpdateCartHandler> updateCartResponseListener =
            new StateUpdateHandlerListener<CartFragment2, UpdateCartHandler>() {
                @Override
                public void onUpdate(String key, CartFragment2 handler, UpdateCartHandler response) {
                    handler.dealUpdateCart(response);
                }

                @Override
                public void onCleanup(String key, CartFragment2 handler, UpdateCartHandler response) {
                    response.removeCompletionListener(handler.cartDataManagerUpdateListener);
                }
            };
    private UpdateCompleteCallback<RetrievedUpdateHandler<CartDataManager>> cartDataManagerUpdateListener =
            new UpdateCompleteCallback<RetrievedUpdateHandler<CartDataManager>>() {
                @Override
                public void onCompleted(RetrievedUpdateHandler<CartDataManager> response,
                                        boolean isSuccess) {
                    dealUpdateCart(response);
                }
            };

    private static StateUpdateHandlerListener<CartFragment2, UpdateItemQuantityHandler> updateQuantitiesHandlerListener =
            new StateUpdateHandlerListener<CartFragment2, UpdateItemQuantityHandler>() {
                @Override
                public void onUpdate(String key, CartFragment2 handler, UpdateItemQuantityHandler response) {
                    handler.dealUpdateQuantities(response);
                }

                @Override
                public void onCleanup(String key, CartFragment2 handler, UpdateItemQuantityHandler response) {
                    response.removeCompletionListener(handler.updateQuantitiesListener);
                }
            };
    private UpdateCompleteCallback<BooleanUpdateHandler> updateQuantitiesListener =
            new UpdateCompleteCallback<BooleanUpdateHandler>() {
                @Override
                public void onCompleted
                        (BooleanUpdateHandler response, boolean isSuccess) {
                    dealUpdateQuantities(response);
                }
            };

    private static StateUpdateHandlerListener<CartFragment2, RemoveCartItemsHandler> removeItemHandlerListener =
            new StateUpdateHandlerListener<CartFragment2, RemoveCartItemsHandler>() {
                @Override
                public void onUpdate(String key, CartFragment2 handler, RemoveCartItemsHandler response) {

                    handler.dealRemoveCartItem(response);
                }

                @Override
                public void onCleanup(String key, CartFragment2 handler, RemoveCartItemsHandler
                        response) {
                    response.removeCompletionListener(handler.removeCartItemListener);
                }
            };
    private UpdateCompleteCallback<BooleanUpdateHandler> removeCartItemListener =
            new UpdateCompleteCallback<BooleanUpdateHandler>() {
                @Override
                public void onCompleted
                        (BooleanUpdateHandler response, boolean isSuccess) {
                    dealRemoveCartItem(response);
                }
            };

    private Delegate<CartDataManager> cartDataListener = new Delegate<CartDataManager>() {
        @Override
        public void execute(CartDataManager cartDataManager) {
            updateViewFromCartData(cartDataManager);
        }
    };

    private CartItemAdapter adapter;
    private ProgressBar progressBar;
    private ListView cartListView;

    View emptyCartView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_cart_loading);
        cartListView = (ListView) view.findViewById(R.id.fragment_cart_list_view);
        emptyCartView = view.findViewById(R.id.cart_empty_container);

        adapter = new CartItemAdapter(getContext(), this);
        cartListView.setAdapter(adapter);
    }

    protected void updateViewFromCartData(final CartDataManager cartDataManager) {
        if (this.adapter != null) {
            ThreadingUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    boolean isEmpty = cartDataManager.getItemList().size() == 0;
                    adapter.setData(cartDataManager.getItemList());
                    ViewUtils.setVisibleOrGone(emptyCartView, isEmpty);
                    ViewUtils.setVisibleOrGone(cartListView, !isEmpty);

                }
            });
        }
//        updateProgressBarState();
    }

    @Override
    public void onPause() {
        super.onPause();
        CartDataManager.getInstance().removeCartItemListener(cartDataListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        CartDataManager.getInstance().addCartItemListener(cartDataListener);
        updateViewFromCartData(CartDataManager.getInstance());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cart, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_clear_cart) {
            CheckoutDataManager.getInstance().clear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemMenu(View view) {

    }

    @Override
    public void onItemQuantityChanged(CartItem cartItem, int newQuantity) {
        OrderType orderType = CheckoutDataManager.getInstance().getSelectedOrderType().getOrderType();
        if (orderType == OrderType.REFUND) {
            newQuantity = 0 - newQuantity;
        }
        updateQuantities(cartItem, newQuantity);
    }

    private void updateQuantities(CartItem cartItem, int newQuantity) {
        if (!isProgressBarShow()) {
            putLoadingItem("CartFragment:QuantitiesUpdating");
            UpdateItemQuantityHandler handler = CheckoutDataManager.getInstance()
                    .updateItemQuantity(cartItem, newQuantity);
            updateForResponse("CartFragment:QuantitiesUpdating", handler,
                    updateQuantitiesHandlerListener);
            handler.run();
        }
    }

    private void dealUpdateQuantities(BooleanUpdateHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog();
                handler.addCompletionListener(updateQuantitiesListener);
            } else {
                if (handler.isSuccess()) {
                    showToast("修改成功");
                } else {
                    showToast("修改失败");
                }
                removeLoadingItem("CartFragment:QuantitiesUpdating");
                cleanupResponse("CartFragment:QuantitiesUpdating");
//                updateProgressBarState();
                updateCart();
                dismissProgressDialog();
            }
        }
    }

    private void updateCart() {
        if ((!isPermanentDeletions())) {
            putLoadingItem("CartFragment:cartUpdateLoading");
            UpdateCartHandler handler = CartDataManager.getInstance().updateCartData();
            updateForResponse("CartFragment:CartUpdate",
                    handler, updateCartResponseListener);
            handler.run();
        }
    }

    private void dealUpdateCart(RetrievedUpdateHandler response) {
        if (response == null) {
            return;
        }
        synchronized (response.getStatusLock()) {
            if (response.isUpdating()) {
                updateProgressBarState();
                response.addCompletionListener(this.cartDataManagerUpdateListener);
            } else {
                if (response.isSuccess()) {
//                  showToast("购物车数据已更新");
                } else {
                    showToast("更新购物车失败");
                }
                cleanupResponse("CartFragment:CartUpdate");
                removeLoadingItem("CartFragment:cartUpdateLoading");
                updateProgressBarState();
            }
        }
    }

    private void removeItem(CartItem cartItem) {
        putLoadingItem("CartFragment:RemovingItem");
        RemoveCartItemsHandler handler = CheckoutDataManager.getInstance().removeCartItem(cartItem);
        updateForResponse("CartFragment:RemoveItem", handler, removeItemHandlerListener);
        handler.run();
    }

    private void dealRemoveCartItem(BooleanUpdateHandler response) {
        if (response == null) {
            return;
        }
        if (response.isUpdating()) {
            updateProgressBarState();
            response.addCompletionListener(removeCartItemListener);
        } else {
            if (!response.isSuccess()) {
                showToast("删除商品失败");
                cleanupResponse("CartFragment:RemoveItem");
            }
            removeLoadingItem("CartFragment:RemovingItem");
            getArguments().putStringArrayList("CartFragment:PermanentDeletions", null);
            updateProgressBarState();
        }
    }

    private void updateProgressBarState() {
        ThreadingUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (progressBar != null) {
                    ViewUtils.setVisibleOrGone(progressBar, isProgressBarShow());
                }
            }
        });
    }

    private boolean isProgressBarShow() {
        String[] loadingKeys = getArguments().getStringArray("CartFragment:loadingItemsArray");
        return ((loadingKeys != null) && loadingKeys.length != 0)
                || isPermanentDeletions();
    }

    private boolean isPermanentDeletions() {
        ArrayList<String> list = getArguments().getStringArrayList("CartFragment:PermanentDeletions");
        return list != null && !list.isEmpty();
    }

    /** 将正在加载的过程状态移除 */
    private void removeLoadingItem(String loadingKey) {
        String[] loadingKeys = getArguments().getStringArray("CartFragment:loadingItemsArray");
        if (loadingKeys != null) {
            ArrayList<String> loadingKeyList = new ArrayList<>(Arrays.asList(loadingKeys));
            loadingKeyList.remove(loadingKey);
            getArguments().putStringArray("CartFragment:loadingItemsArray",
                    loadingKeyList.toArray(new String[loadingKeyList.size()]));
        }
    }

    /** 将正在加载的过程状态移除保存 */
    private void putLoadingItem(String loadingKey) {
        String[] loadingKeys = getArguments().getStringArray("CartFragment:loadingItemsArray");
        if (loadingKeys == null) {
            loadingKeys = new String[0];
        }
        ArrayList<String> loadingKeyList = new ArrayList<>(Arrays.asList(loadingKeys));
        if (!loadingKeyList.contains(loadingKey)) {
            loadingKeyList.add(loadingKey);
        }
        getArguments().putStringArray("CartFragment:loadingItemsArray",
                loadingKeyList.toArray(new String[loadingKeyList.size()]));
    }
}
