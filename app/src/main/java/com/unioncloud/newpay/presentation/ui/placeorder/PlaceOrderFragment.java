package com.unioncloud.newpay.presentation.ui.placeorder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.esummer.android.dialog.DefaultDialogBuilder;
import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.raizlabs.coreutils.functions.Delegate;
import com.raizlabs.coreutils.threading.ThreadingUtils;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.cart.AddProductEntry;
import com.unioncloud.newpay.domain.model.erp.VipCard;
import com.unioncloud.newpay.domain.model.notice.Notice;
import com.unioncloud.newpay.domain.model.order.OrderType;
import com.unioncloud.newpay.domain.model.product.Product;
import com.unioncloud.newpay.presentation.model.cart.CartDataManager;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.checkout.SelectedVipCard;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.cart.AddCartItemsHandler;
import com.unioncloud.newpay.presentation.presenter.notice.QueryNewNoticeHandler;
import com.unioncloud.newpay.presentation.ui.cart.CartFragment2;
import com.unioncloud.newpay.presentation.ui.cart.views.QuantityAdapter;
import com.unioncloud.newpay.presentation.ui.checkout.CheckoutActivity;
import com.unioncloud.newpay.presentation.ui.notice.NoticeDetailFragment;
import com.unioncloud.newpay.presentation.ui.notice.NoticesFragment;
import com.unioncloud.newpay.presentation.ui.vip.QueryVipActivity;
import com.unioncloud.newpay.presentation.ui.vip.QueryVipFragment;

import java.util.List;

/**
 * Created by cwj on 16/8/9.
 */
public class PlaceOrderFragment extends StatedFragment {

    public static PlaceOrderFragment getInstance() {
        return getInstance(OrderType.SALE);
    }

    public static PlaceOrderFragment getInstance(OrderType orderType) {
        PlaceOrderFragment fragment = new PlaceOrderFragment();
        if (orderType == null) {
            orderType = OrderType.SALE;
        }
        fragment.getArguments().putSerializable("orderType", orderType);
        return fragment;
    }

    private StateUpdateHandlerListener<PlaceOrderFragment, AddCartItemsHandler> addItemHandlerListenr =
            new StateUpdateHandlerListener<PlaceOrderFragment, AddCartItemsHandler>() {
                @Override
                public void onUpdate(String key, PlaceOrderFragment handler, AddCartItemsHandler response) {
                    handler.dealAddCartItem(response);
                }

                @Override
                public void onCleanup(String key, PlaceOrderFragment handler, AddCartItemsHandler response) {
                    response.removeCompletionListener(handler.addItemListener);
                }
            };
    private UpdateCompleteCallback<AddCartItemsHandler> addItemListener =
            new UpdateCompleteCallback<AddCartItemsHandler>() {
                @Override
                public void onCompleted(AddCartItemsHandler response, boolean isSuccess) {
                    dealAddCartItem(response);
                }
            };

    private static StateUpdateHandlerListener<PlaceOrderFragment, QueryNewNoticeHandler> noticeHandlerListener =
            new StateUpdateHandlerListener<PlaceOrderFragment, QueryNewNoticeHandler>() {
                @Override
                public void onUpdate(String key, PlaceOrderFragment handler, QueryNewNoticeHandler response) {
                    handler.dealNotice(response);
                }

                @Override
                public void onCleanup(String key, PlaceOrderFragment handler, QueryNewNoticeHandler response) {
                    response.removeCompletionListener(handler.noticeListener);
                }
            };
    private UpdateCompleteCallback<QueryNewNoticeHandler> noticeListener =
            new UpdateCompleteCallback<QueryNewNoticeHandler>() {
                @Override
                public void onCompleted(QueryNewNoticeHandler response, boolean isSuccess) {
                    dealNotice(response);
                }
            };

    private ProductChooseViewBinder productChooseViewBinder;
    private VipCardViewBinder vipCardViewBinder;
    private ProductAdapter adapter;

    private Delegate<SelectedVipCard> selectedVipListener = new Delegate<SelectedVipCard>() {
        @Override
        public void execute(SelectedVipCard selectedVipCard) {
            updateVipCardView(selectedVipCard);
        }
    };

    private static final int REQUEST_QUERY_VIP_CODE = 2000;
    private static final int DIALOG_EMPTY_CART = 0X111;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_place_order, container, false);
        productChooseViewBinder = new ProductChooseViewBinder();
        return view;
    }

    private QueryVipFragment getAddVipFragment() {
        Fragment fragment = getChildFragmentManager().findFragmentByTag("PlaceOrder:QueryVipFragment");
        if ((fragment != null) && ((fragment instanceof QueryVipFragment))) {
            return (QueryVipFragment)fragment;
        }
        return null;
    }

    private void showAddVip() {
        Intent intent = QueryVipActivity.getStartIntent(getActivity());
        startActivityForResult(intent, REQUEST_QUERY_VIP_CODE);
    }

    private void initCartFragment() {
        CartFragment2 cartFragment = (CartFragment2) getChildFragmentManager()
                .findFragmentByTag("TAG_CART_FRAGMENT");
        if (cartFragment == null) {
            cartFragment = CartFragment2.getInstance();
        }
        if (!cartFragment.isAdded()) {
            getChildFragmentManager().beginTransaction().add(R.id.fragment_place_order_cart_frame,
                    cartFragment, "TAG_CART_FRAGMENT").commit();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productChooseViewBinder.bindView(view);
        productChooseViewBinder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
        productChooseViewBinder.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCheckout();
            }
        });
        adapter = new ProductAdapter();
        productChooseViewBinder.setAdapter(adapter);
        productChooseViewBinder.quantitySpinner.setAdapter(new QuantityAdapter());
        vipCardViewBinder = new VipCardViewBinder(view);
        vipCardViewBinder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearVip();
            }
        });
    }

    private void initOrderType() {
        OrderType orderType = (OrderType) getArguments().getSerializable("orderType");
        switch (orderType) {
            case SALE:
                setTitle("销售单");
                CheckoutDataManager.getInstance().getSelectedOrderType().setOrderType(OrderType.SALE);
                break;
            case REFUND:
                setTitle("退货单");
                CheckoutDataManager.getInstance().getSelectedOrderType().setOrderType(OrderType.REFUND);
                break;
        }
    }

    private void clearVip() {
        CheckoutDataManager.getInstance().getSelectedVipCard().setSelectedVipCard(null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        loadNewNotice();
    }

    private void initData() {
        CheckoutDataManager.getInstance().clear();
        selectOrderType();
        updateChooseView(PosDataManager.getInstance());
    }

    private void selectOrderType() {
        OrderType orderType = (OrderType) getArguments().getSerializable("orderType");
        CheckoutDataManager.getInstance().getSelectedOrderType().setOrderType(orderType);
    }

    private void addToCart() {
//        Product product = (Product) productChooseViewBinder.productNoSpinner.getSelectedItem();
        Product product = (Product) productChooseViewBinder.getSelectedItem();
        if (product != null) {
            int price ;
            if (product.isFixPrice()) {
                price = product.getPrice();
            } else {
                price = productChooseViewBinder.getCustomPrice();
            }
            if (price == 0) {
                showToast("无效的价格");
                return;
            }
            int quantity = productChooseViewBinder.quantitySpinner.getSelectedItemPosition() + 1;
            OrderType orderType = CheckoutDataManager.getInstance().getSelectedOrderType().getOrderType();
            if (orderType == OrderType.REFUND) {
                quantity = 0 - quantity;
            }
            AddProductEntry entry = new AddProductEntry(product, quantity, price);
            requestAddCartItem(entry);
        }
    }

    private void toCheckout() {
        boolean isEmptyCart = CartDataManager.getInstance().isEmpty();
        if (isEmptyCart) {
            showToast("购物车空空的!");
            return;
        }
        Intent intent = CheckoutActivity.getStartIntent(getActivity());
        startActivity(intent);
    }

    private void requestAddCartItem(AddProductEntry entry) {
        AddCartItemsHandler handler = new AddCartItemsHandler(entry);
        updateForResponse("ADD_CART_ITEM", handler, addItemHandlerListenr);
        handler.run();
    }

    private void dealAddCartItem(AddCartItemsHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog();
                handler.addCompletionListener(addItemListener);
            } else {
                if (handler.isSuccess()) {
                    hideSoftInput();
                    productChooseViewBinder.resetView();    // 重置选购界面
                } else {
                    showToast("添加商品失败");
                }
                dismissProgressDialog();
                cleanupResponse("ADD_CART_ITEM");
            }
        }
    }

    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(productChooseViewBinder.priceEt.getWindowToken(), 0);
    }

    private void updateChooseView(PosDataManager posDataManager) {
        List<Product> list = PosDataManager.getInstance().getProductList();
        adapter.setDataList(list);
        if (list.size() != 0) {
//            productChooseViewBinder.selectedProduct(list.get(0));
            productChooseViewBinder.selectedProduct(null);
        } else {
            productChooseViewBinder.selectedProduct(null);
        }
    }

    private void updateVipCardView(SelectedVipCard selectedVipCard) {
        if (isVisible()) {
            final VipCard vipCard = selectedVipCard.getVipCard();
            if (vipCardViewBinder != null) {
                ThreadingUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (vipCardViewBinder != null) {
                            vipCardViewBinder.selectedVipCard(vipCard);
                        }
                    }
                });
            }
        } else {
            getArguments().putBoolean("needUpdateVipView", true);
        }
    }


    private boolean needUpdateVipView() {
        if (getArguments().containsKey("needUpdateVipView")) {
            return getArguments().getBoolean("needUpdateVipView");
        }
        return false;
    }

    private void removeUpdateVipViewFlag() {
        getArguments().remove("needUpdateVipView");
    }

    @Override
    public void onResume() {
        super.onResume();
        initCartFragment();
        initOrderType();
        CheckoutDataManager.getInstance().getSelectedVipCard()
                .addDataChangedListener(selectedVipListener);
        if (needUpdateVipView()) {
            updateVipCardView(CheckoutDataManager.getInstance().getSelectedVipCard());
            removeUpdateVipViewFlag();
        }
    }

    @Override
    public boolean onBackPressed() {
        showExit();
        return true;
    }

    private void showExit() {
        if (!CartDataManager.getInstance().isEmpty()) {
            if (!hasEmptyCartDialog()) {
                DefaultDialogBuilder builder = createAndSaveDialogBuilder(DIALOG_EMPTY_CART);
                builder.setTitle("清空购物车");
                builder.setMessage("购物车已存放商品,退出将清空购物车!");
                showDialog(builder);
            }
        } else {
            getActivity().finish();
        }
    }

    @Override
    protected void onDialogCancelClicked(int dialogId) {
        if (dialogId == DIALOG_EMPTY_CART) {
            getArguments().remove("DIALOG_EMPTY_CART");
            dismissDialog(getSavedBuilder(dialogId));
            return;
        }
        super.onDialogCancelClicked(dialogId);
    }

    @Override
    protected void onDialogOkClicked(int dialogId) {
        if (dialogId == DIALOG_EMPTY_CART) {
            CartDataManager.getInstance().clear();
            getArguments().remove("DIALOG_EMPTY_CART");
            dismissDialog(getSavedBuilder(dialogId));
            getActivity().finish();
            return;
        }
        super.onDialogOkClicked(dialogId);
    }

    private boolean hasEmptyCartDialog() {
        return getArguments().getBoolean("DIALOG_EMPTY_CART");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        productChooseViewBinder = null;
        vipCardViewBinder = null;
        CheckoutDataManager.getInstance().getSelectedVipCard()
                .removeDataChangedListener(selectedVipListener);
        QueryVipFragment fragment = getAddVipFragment();
        if (fragment != null) {
            fragment.dismiss();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.place_order_menu, menu);
        inflater.inflate(R.menu.cart, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_vip:
                showAddVip();
                return true;
            case R.id.action_clear_cart:
                CheckoutDataManager.getInstance().clear();
                productChooseViewBinder.resetView();
                initOrderType();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_QUERY_VIP_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                VipCard vipCard = (VipCard) data.getSerializableExtra("vip");
                if (vipCard != null) {
                    CheckoutDataManager.getInstance()
                            .getSelectedVipCard().setSelectedVipCard(vipCard);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadNewNotice() {
        QueryNewNoticeHandler handler = new QueryNewNoticeHandler(null);
        updateForResponse("KEY_LOAD_NEW_NOTICES", handler, noticeHandlerListener);
        handler.run();
    }

    private void dealNotice(QueryNewNoticeHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                handler.addCompletionListener(noticeListener);
            } else {
                dismissProgressDialog();
                cleanupResponse("KEY_LOAD_NEW_NOTICES");
                if (handler.isSuccess()) {
                    showNewNotice(handler.getData());
                }
            }
        }
    }

    private void showNewNotice(List<Notice> list) {
        if (list == null){
            return;
        }
        if (list.size() == 0) {
            NoticeDetailFragment fragment = NoticeDetailFragment.newInstance(list.get(0));
            fragment.show(getChildFragmentManager(), "notice_detail");
        } else {
            NoticesFragment fragment = NoticesFragment.newInstance();
            fragment.setDialogMode(true);
            fragment.setNotices(list);
            fragment.show(getChildFragmentManager(), "notices");
        }
    }
}
