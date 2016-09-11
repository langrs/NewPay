package com.unioncloud.newpay.presentation.ui.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.RetrievedUpdateHandler;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.esummer.android.widget.LoadingBananaPeelView;
import com.raizlabs.coreutils.functions.Delegate;
import com.raizlabs.coreutils.threading.ThreadingUtils;
import com.raizlabs.coreutils.view.ViewUtils;
import com.raizlabs.universaladapter.ListBasedAdapter;
import com.raizlabs.universaladapter.converter.BaseAdapterConverter;
import com.raizlabs.universaladapter.converter.UniversalConverterFactory;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.presentation.presenter.BooleanUpdateHandler;
import com.unioncloud.newpay.presentation.model.cart.CartDataManager;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.presenter.cart.RemoveCartItemsHandler;
import com.unioncloud.newpay.presentation.presenter.cart.UpdateCartHandler;
import com.unioncloud.newpay.presentation.presenter.cart.UpdateItemQuantityHandler;
import com.unioncloud.newpay.presentation.presenter.checkout.CalculateAmountsHandler;
import com.unioncloud.newpay.presentation.ui.cart.views.CartItem2Adapter;
import com.unioncloud.newpay.presentation.ui.cart.views.CartItemViewHolder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by cwj on 16/8/9.
 */
public class CartFragment extends StatedFragment implements CartItemMenuListener {

    private static StateUpdateHandlerListener<CartFragment, CalculateAmountsHandler> calculateAmountResponseListener =
            new StateUpdateHandlerListener<CartFragment, CalculateAmountsHandler>() {
                @Override
                public void onUpdate(String key, CartFragment handler, CalculateAmountsHandler response) {
                    handler.dealCalculateAmounts(response);
                }

                @Override
                public void onCleanup(String key, CartFragment handler, CalculateAmountsHandler response) {
                    response.removeCompletionListener(handler.calculateAmountUpdateListener);
                }
            };
    private UpdateCompleteCallback<CalculateAmountsHandler> calculateAmountUpdateListener =
            new UpdateCompleteCallback<CalculateAmountsHandler>() {
                @Override
                public void onCompleted(CalculateAmountsHandler response, boolean isSuccess) {
                    dealCalculateAmounts(response);
                }
            };

    private Delegate<CartDataManager> cartDataListener = new Delegate<CartDataManager>() {
        @Override
        public void execute(CartDataManager cartDataManager) {
            updateViewFromCartData(cartDataManager);
        }
    };
    private static StateUpdateHandlerListener<CartFragment, UpdateCartHandler> updateCartResponseListener =
            new StateUpdateHandlerListener<CartFragment, UpdateCartHandler>() {
                @Override
                public void onUpdate(String key, CartFragment handler, UpdateCartHandler response) {
                    handler.dealUpdateCart(response);
                }

                @Override
                public void onCleanup(String key, CartFragment handler, UpdateCartHandler response) {
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


    private PopupMenu cartItemMenu;
    private UpdateCompleteCallback<BooleanUpdateHandler> removeCartItemListener =
            new UpdateCompleteCallback<BooleanUpdateHandler>() {
                @Override
                public void onCompleted
                        (BooleanUpdateHandler response, boolean isSuccess) {
                    dealRemoveCartItem(response);
                }
            };

    private static StateUpdateHandlerListener<CartFragment, UpdateItemQuantityHandler> updateQuantitiesHandlerListener =
            new StateUpdateHandlerListener<CartFragment, UpdateItemQuantityHandler>() {
                @Override
                public void onUpdate(String key, CartFragment handler, UpdateItemQuantityHandler response) {
                    handler.dealUpdateQuantities(response);
                }

                @Override
                public void onCleanup(String key, CartFragment handler, UpdateItemQuantityHandler response) {
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


//    private boolean isPreSaleMode;  // 是否是预结算的购物车模式

//    private LoadingBananaPeelView c;
    private LoadingBananaPeelView loadingBananaPeelView;

//    private ProgressBar m;
    private ProgressBar progressBar;

    // private c.a.a.a p'
    private ListView itemListView;


//    private View checkoutTooltip
    private BaseAdapterConverter<CartItem, CartItemViewHolder> adapter;


    private ViewGroup cartContentContainer;

    private Button addVipButton;

    private OrderTotalViewBinder orderTotalViewBinder;

    private FloatingActionButton checkoutButton;  // 结算按钮(跳转到支付界面)

    public static CartFragment getInstance() {
        CartFragment fragment = new CartFragment();
        return fragment;
    }

    public static CartFragment getPreSaleModeInstance() {
        CartFragment fragment = new CartFragment();
        fragment.getArguments().putBoolean("KEY_CART_IS_PRESALE_MODE", true);
        return fragment;
    }

    private boolean isPreSaleMode() {
        return getArguments().getBoolean("KEY_CART_IS_PRESALE_MODE", false);
    }

    private void setIsPreSaleMode(boolean isPreSaleMode) {
        getArguments().putBoolean("KEY_CART_IS_PRESALE_MODE", isPreSaleMode);
    }

    private void removeItem(CartItem cartItem) {
        putLoadingItem("CartFragment:RemovingItem");
        RemoveCartItemsHandler handler = CheckoutDataManager.getInstance().removeCartItem(cartItem);
        updateForResponse("CartFragment:RemoveItem", handler,
                new StateUpdateHandlerListener<CartFragment, RemoveCartItemsHandler>() {
            @Override
            public void onUpdate(String key, CartFragment handler, RemoveCartItemsHandler response) {
                handler.dealRemoveCartItem(response);
            }

            @Override
            public void onCleanup(String key, CartFragment handler, RemoveCartItemsHandler response) {
                response.removeCompletionListener(removeCartItemListener);
            }
        });
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

    private boolean isProgressBarShow() {
        String[] loadingKeys = getArguments().getStringArray("CartFragment:loadingItemsArray");
        return ((loadingKeys != null) && loadingKeys.length != 0)
                || isPermanentDeletions();
    }

    private boolean isPermanentDeletions() {
        ArrayList<String> list = getArguments().getStringArrayList("CartFragment:PermanentDeletions");
        return list != null && !list.isEmpty();
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
//                  showUpdateCartAnimation();
                } else {
                    showToast("更新购物车失败");
                }
                cleanupResponse("CartFragment:CartUpdate");
                removeLoadingItem("CartFragment:cartUpdateLoading");
                updateProgressBarState();
            }
        }
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

    private void calculateAmounts() {
        if ((!isProgressBarShow())) {
            putLoadingItem("CartFragment:calculateAmountsLoading");
            updateForResponse("CartFragment:UpdateTotalsResponse",
                    CheckoutDataManager.getInstance().calculateAmounts(), calculateAmountResponseListener);
        }
    }

    private void dealCalculateAmounts(CalculateAmountsHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                handler.addCompletionListener(calculateAmountUpdateListener);
                updateProgressBarState();
            } else {
                if (handler.isSuccess() && orderTotalViewBinder != null) {
                    orderTotalViewBinder.setOrderTotal(handler.getData());
                }
                cleanupResponse("CartFragment:UpdateTotalsResponse");
                removeLoadingItem("CartFragment:calculateAmountsLoading");
                updateProgressBarState();
            }
        }
    }

    protected void updateViewFromCartData(final CartDataManager cartDataManager) {
        if (this.adapter != null) {
            ThreadingUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    ((ListBasedAdapter)adapter.getAdapter()).loadItemList(cartDataManager.getItemList());
                    if (cartDataManager.getItemList().size() > 0) {
                        loadingBananaPeelView.displayContentView();
                    } else {
                        showCartEmptyView();
                    }
                }
            });
        }
        updateProgressBarState();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container ,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadingBananaPeelView = (LoadingBananaPeelView) view.findViewById(R.id.fragment_cart_banana);

        View loadingContentView = loadingBananaPeelView.getContentView();
        itemListView = (ListView) loadingContentView.findViewById(R.id.fragment_cart_list_view);
        adapter = UniversalConverterFactory.create(new CartItem2Adapter(this), itemListView);

        progressBar = (ProgressBar) loadingContentView.findViewById(R.id.fragment_cart_loading);
//        CoordinatorLayout coordinatorLayout =
//                (CoordinatorLayout)loadingContentView.findViewById(R.id.fragment_cart_coordinatorLayout);

        View cartFooterView = itemListView.findViewById(R.id.fragment_cart_footer_contents);
        if (cartFooterView == null) {
            cartFooterView = LayoutInflater.from(getActivity()).
                    inflate(R.layout.view_cart_footer_contents, itemListView, false);
            itemListView.addFooterView(cartFooterView);
        }
        ViewUtils.setVisibleOrGone(cartFooterView, isPreSaleMode());

        addVipButton = (Button) cartFooterView.findViewById(R.id.fragment_cart_edit_vip_button);
        addVipButton.setOnClickListener(null);
        orderTotalViewBinder = new OrderTotalViewBinder(cartFooterView);

        cartContentContainer = (ViewGroup) view.findViewById(R.id.fragment_cart_contents_container);
        checkoutButton = (FloatingActionButton) cartContentContainer.findViewById(R.id.fragment_cart_payment_options_button);
        ViewUtils.setVisibleOrGone(checkoutButton, isPreSaleMode());

        updateCart();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cart, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_clear_cart) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cartItemMenu != null) {
            cartItemMenu.dismiss();
            cartItemMenu = null;
        }
        adapter = null;
        progressBar = null;
        itemListView = null;
    }

//    private void q()
    private void showCartEmptyView() {
        if (this.loadingBananaPeelView != null) {
            ThreadingUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    loadingBananaPeelView.setBananaPeelMessage(R.string.Cart_Empty_Title);
//                    loadingBananaPeelView.setBananaPeelMsgDetailVisibility(true);
//                    loadingBananaPeelView.setBananaPeelMessageDetail("选择商品添加到购物车");
                    loadingBananaPeelView.displayBananaPeelView();
                    loadingBananaPeelView.setBananaPeelListener(null); // 设置点击监听
                }
            });
        }
    }

//    private void r()
    private void showCartErrorView() {
        if (loadingBananaPeelView != null) {
            ThreadingUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    loadingBananaPeelView.setBananaPeelMsgDetailVisibility(false);
                    loadingBananaPeelView.setBananaPeelMessage("获取购物车数据失败");
                    loadingBananaPeelView.displayBananaPeelView();
                    loadingBananaPeelView.setBananaPeelListener(null); // 设置点击监听
                }
            });
        }
    }

    @Override
    public void onItemMenu(View view) {
        if (view != null && view.getTag() != null) {
            if (cartItemMenu != null) {
                cartItemMenu.dismiss();
            }
            this.cartItemMenu = new PopupMenu(getActivity(), view);
            cartItemMenu.getMenuInflater().inflate(R.menu.cart_item, cartItemMenu.getMenu());
            final CartItem cartItem = (CartItem) view.getTag();
            if (cartItem.isFixPrice()) {
                cartItemMenu.getMenu().removeItem(R.id.action_modify_price);
            }
            cartItemMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_modify_price:
                            // TODO
                            return true;
                        case R.id.action_delete:
                            removeItem(cartItem);
                            return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onItemQuantityChanged(CartItem cartItem, int newQuantity) {
        updateQuantities(cartItem, newQuantity);
    }
}
