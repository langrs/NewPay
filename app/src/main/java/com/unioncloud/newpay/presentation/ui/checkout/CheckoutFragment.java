package com.unioncloud.newpay.presentation.ui.checkout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.esummer.android.dialog.DefaultDialogBuilder;
import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.erp.VipCard;
import com.unioncloud.newpay.domain.model.order.OrderType;
import com.unioncloud.newpay.domain.model.order.SaleOrderResult;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.print.PrintOrderInfo;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.presentation.model.cart.CartDataManager;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.checkout.OrderTotals;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.checkout.CalculateAmountsHandler;
import com.unioncloud.newpay.presentation.presenter.checkout.SubmitOrderHandler;
import com.unioncloud.newpay.presentation.presenter.print.PrintOrderHandler;
import com.unioncloud.newpay.presentation.presenter.print.PrintOrderHandlerNew;
import com.unioncloud.newpay.presentation.ui.cart.OrderTotalViewBinder;
import com.unioncloud.newpay.presentation.ui.pay.PayActivity;
import com.unioncloud.newpay.presentation.ui.pay.PaymentSignpost;
import com.unioncloud.newpay.presentation.ui.right.QueryRightActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.data;

/**
 * Created by cwj on 16/8/15.
 */
public class CheckoutFragment extends StatedFragment {

    public static CheckoutFragment newInstance() {
        CheckoutFragment fragment = new CheckoutFragment();
        return fragment;
    }

    private static StateUpdateHandlerListener<CheckoutFragment, CalculateAmountsHandler>
            calculateAmountResponseListener =
            new StateUpdateHandlerListener<CheckoutFragment, CalculateAmountsHandler>() {
                @Override
                public void onUpdate(String key, CheckoutFragment handler, CalculateAmountsHandler response) {
                    handler.dealCalculateAmounts(response);
                }

                @Override
                public void onCleanup(String key, CheckoutFragment handler, CalculateAmountsHandler response) {
                    response.removeCompletionListener(handler.calculateAmountUpdateListener);
                }
            };
    private        UpdateCompleteCallback<CalculateAmountsHandler>
            calculateAmountUpdateListener   =
            new UpdateCompleteCallback<CalculateAmountsHandler>() {
                @Override
                public void onCompleted(CalculateAmountsHandler response, boolean isSuccess) {
                    dealCalculateAmounts(response);
                }
            };


    private static StateUpdateHandlerListener<CheckoutFragment, SubmitOrderHandler> submitHandlerListener =
            new StateUpdateHandlerListener<CheckoutFragment, SubmitOrderHandler>() {
                @Override
                public void onUpdate(String key, CheckoutFragment handler, SubmitOrderHandler response) {
                    handler.dealSubmit(response);
                }

                @Override
                public void onCleanup(String key, CheckoutFragment handler, SubmitOrderHandler response) {
                    response.removeCompletionListener(handler.submitListener);
                }
            };
    private        UpdateCompleteCallback<SubmitOrderHandler>                       submitListener        =
            new UpdateCompleteCallback<SubmitOrderHandler>() {
                @Override
                public void onCompleted(SubmitOrderHandler response, boolean isSuccess) {
                    dealSubmit(response);
                }
            };

    private static final int REQUEST_RETRY_CALCULATE_AMOUNT = 0x111;
    private static final int REQUEST_RETRY_SUBMIT           = 0x222;
    private static final int REQUEST_TO_PAY                 = 0x3333;
    private static final int REQUEST_FILLIN_RIGHT           = 0x0004;

    private static final String KEY_LOADING_ARRAY = "CheckoutFragment:loadingItemsArray";

    private PaidTotalViewBinder  paidTotalViewBinder;
    private OrderTotalViewBinder orderTotalViewBinder;
    private Switch               fillInSwitch;

    private ListView       paymentsListView;
    private PaymentAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle("结算");

        paidTotalViewBinder = new PaidTotalViewBinder(view);
        orderTotalViewBinder = new OrderTotalViewBinder(view);
        paymentsListView = (ListView) view.findViewById(R.id.fragment_checkout_payment_list);
        adapter = new PaymentAdapter(getActivity());
        paymentsListView.setAdapter(adapter);
        paymentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PaymentSignpost signpost = adapter.getItem(position);
                showPayment(signpost);
            }
        });
        fillInSwitch = (Switch) view.findViewById(R.id.fragment_checkout_fillin_control);
        fillInSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    requestRefundRight();
                }
            }
        });
    }

    private boolean isFillInOpen() {
        return fillInSwitch.isChecked();
    }

    private void requestRefundRight() {
        Intent intent = QueryRightActivity.getStartIntent(getActivity());
        startActivityForResult(intent, REQUEST_FILLIN_RIGHT);
    }

    private void showPayment(PaymentSignpost signpost) {
        OrderType orderType = CheckoutDataManager.getInstance().getSelectedOrderType().getOrderType();
        if (orderType == OrderType.SALE) {
            if (hasSameThridPartyPayment(signpost)) {
                showToast("第三方在线支付只能使用一个");
                return;
            }
            if (signpost.supportPay()) {
                Intent intent = PayActivity.getStartIntent(getActivity(), signpost, isFillInOpen());
                startActivityForResult(intent, REQUEST_TO_PAY);
            } else {
                showToast("暂不支持该支付方式");
            }
        } else {
            // TODO 录单退货
//            fragment = signpost.toRefund();
        }
    }

    // 威富通的第三方支付, 一个订单只能支付一次
    private boolean hasSameThridPartyPayment(PaymentSignpost signpost) {
        if (signpost == PaymentSignpost.ALI ||
                signpost == PaymentSignpost.WECHAT) {
            Payment aliPay = PosDataManager.getInstance().getPaymentByNumberInt(signpost.numberToInt());
            Payment wechatPay = PosDataManager.getInstance().getPaymentByNumberInt(signpost.numberToInt());
            return CheckoutDataManager.getInstance().hasUsedPayment(aliPay)
                    || CheckoutDataManager.getInstance().hasUsedPayment(wechatPay);
        }
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        filterPayment();        // 支付方式不会经常变更,只在创建的时候更新
        requestCalculateAmounts();
    }

    private void filterPayment() {
        List<Payment>         payments     = PosDataManager.getInstance().getPaymentList();
        List<PaymentSignpost> signpostList = PaymentSignpost.filter(payments);
        if (adapter != null) {
            adapter.setDataList(signpostList);
        }
    }

    private void requestCalculateAmounts() {
        if (!isLoading()) {
            putLoadingItem("CheckoutFragment:calculating");
            updateForResponse("CheckoutFragment:calculateAmounts",
                    CheckoutDataManager.getInstance().calculateAmounts(),
                    calculateAmountResponseListener);
        }
    }

    private void dealCalculateAmounts(CalculateAmountsHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("正在生成订单...");
                handler.addCompletionListener(calculateAmountUpdateListener);
            } else {
                dismissProgressDialog();
                removeLoadingItem("CheckoutFragment:calculating");
                cleanupResponse("CheckoutFragment:calculateAmounts");
                if (handler.isSuccess()) {
                    checkOrderPay();
                    updateOrderTotalView();
                } else {
                    showRetryCalculate();
                }
            }
        }
    }

    // 检查订单是否已经支付完成
    private void checkOrderPay() {
        int unpayTotal = CheckoutDataManager.getInstance().getUnpayTotal();
        if (unpayTotal == 0) {
            submitOrder();
        }
        if (paidTotalViewBinder != null) {
            paidTotalViewBinder.setUsedPayments(CheckoutDataManager.getInstance().getUsedPayments());
        }
    }

    private void updateOrderTotalView() {
        if (orderTotalViewBinder != null) {
            orderTotalViewBinder.setOrderTotal(CheckoutDataManager.getInstance().getOrderTotals());
        }
    }

    private void showRetryCalculate() {
        DefaultDialogBuilder builder = createAndSaveDialogBuilder(REQUEST_RETRY_CALCULATE_AMOUNT);
        builder.setTitle("生成订单失败");
        builder.setMessage("请确定尝试生成订单,还是取消订单");
        builder.setCancelable(false);
        showDialog(builder);
    }

    @Override
    protected void onDialogOkClicked(int dialogId) {
        dismissDialog(getSavedBuilder(dialogId));
        switch (dialogId) {
            case REQUEST_RETRY_CALCULATE_AMOUNT:
                requestCalculateAmounts();
                break;
            case REQUEST_RETRY_SUBMIT:
                submitOrder();
                break;
        }
    }

    @Override
    protected void onDialogCancelClicked(int dialogId) {
        if (dialogId == REQUEST_RETRY_CALCULATE_AMOUNT ||
                dialogId == REQUEST_RETRY_SUBMIT) {
            dismissDialog(getSavedBuilder(dialogId));
            CheckoutDataManager.getInstance().clear();
            getActivity().finish();
        }
    }

    private void submitOrder() {
        if (!isLoading()) {
            putLoadingItem("CheckoutFragment:submitOrder");
            SubmitOrderHandler handler = CheckoutDataManager.getInstance().submitOrder();
            updateForResponse("CheckoutFragment:submitOrder",
                    handler,
                    submitHandlerListener);
            handler.run();
        }
    }

    private void dealSubmit(SubmitOrderHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("正在提交销售单...");
                handler.addCompletionListener(submitListener);
            } else {
                removeLoadingItem("CheckoutFragment:submitOrder");
                cleanupResponse("CheckoutFragment:submitOrder");
                if (handler.isSuccess()) {
                    showToast("提交销售单完成");
                    CheckoutDataManager.getInstance().saveLastOrderSerialNumber();
                    toPrintOrder(handler.getData());
                } else {
                    dismissProgressDialog();
                    showRetrySubmit();
                }
            }
        }
    }

    private void showRetrySubmit() {
        DefaultDialogBuilder builder = createAndSaveDialogBuilder(REQUEST_RETRY_SUBMIT);
        builder.setTitle("提交销售单失败");
        builder.setMessage("请确定再次提交,还是取消销售单");
        builder.setCancelable(false);
        showDialog(builder);
    }

    private static StateUpdateHandlerListener<CheckoutFragment, PrintOrderHandler> printHandlerListener =
            new StateUpdateHandlerListener<CheckoutFragment, PrintOrderHandler>() {
                @Override
                public void onUpdate(String key, CheckoutFragment handler, PrintOrderHandler response) {
                    handler.dealPrint(response);
                }

                @Override
                public void onCleanup(String key, CheckoutFragment handler, PrintOrderHandler response) {
                    response.removeCompletionListener(handler.printListener);
                }
            };
    private        UpdateCompleteCallback<PrintOrderHandler>                       printListener        = new
            UpdateCompleteCallback<PrintOrderHandler>() {
                @Override
                public void onCompleted(PrintOrderHandler response, boolean isSuccess) {
                    dealPrint(response);
                }
            };


    private void toPrintOrder(SaleOrderResult saleOrderResult) {

        PrintOrderInfo info = new PrintOrderInfo();
        info.setPosInfo(PosDataManager.getInstance().getPosInfo());
        info.setOrderId(saleOrderResult.getSaleNo());
        info.setSaleTime(saleOrderResult.getSaleDate());
        info.setCartItemList(CartDataManager.getInstance().getItemList());
        VipCard vipCard = CheckoutDataManager.getInstance().getSelectedVipCard().getVipCard();
        if (vipCard != null) {
            info.setVipNo(vipCard.getCardNumber());
        }
        info.setFooter(PosDataManager.getInstance().getPosInfo().getPrintFooter());
        info.setPaymentUsedList(CheckoutDataManager.getInstance().getUsedPayments().getUsedList());
        OrderTotals orderTotals = CheckoutDataManager.getInstance().getOrderTotals();
        info.setTotal(MoneyUtils.fenToString(orderTotals.subtotal));
        info.setPaidTotal(MoneyUtils.fenToString(orderTotals.total));
        info.setDiscountTotal(MoneyUtils.fenToString(orderTotals.discount));
        info.setSalePoints(saleOrderResult.getSalePoints());
        info.setOriginalPoints(saleOrderResult.getOriginalPoints());
        info.setCoupons(saleOrderResult.getCouponList());

        PrintOrderHandler handler = new PrintOrderHandlerNew(getActivity(), info);

        putLoadingItem("CheckoutFragment:PrintOrder");
        updateForResponse("CheckoutFragment:PrintOrder", handler, printHandlerListener);
        handler.print();
    }

    private void dealPrint(PrintOrderHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("正在打印销售单...");
                handler.addCompletionListener(printListener);
            } else {
                dismissProgressDialog();
                removeLoadingItem("CheckoutFragment:PrintOrder");
                cleanupResponse("CheckoutFragment:PrintOrder");
                if (handler.isSuccess() && handler.getData()) {
                    showToast("打印完成");
                } else {
                    showToastLong("打印失败");
                }
                CheckoutDataManager.getInstance().clear();
                getActivity().finish();
            }
        }
    }

    private boolean isLoading() {
        String[] loadingKeys = getArguments().getStringArray(KEY_LOADING_ARRAY);
        return ((loadingKeys != null) && loadingKeys.length != 0);
    }

    /**
     * 将正在加载的过程状态移除保存
     */
    private void putLoadingItem(String loadingKey) {
        String[] loadingKeys = getArguments().getStringArray(KEY_LOADING_ARRAY);
        if (loadingKeys == null) {
            loadingKeys = new String[0];
        }
        ArrayList<String> loadingKeyList = new ArrayList<>(Arrays.asList(loadingKeys));
        if (!loadingKeyList.contains(loadingKey)) {
            loadingKeyList.add(loadingKey);
        }
        getArguments().putStringArray(KEY_LOADING_ARRAY,
                loadingKeyList.toArray(new String[loadingKeyList.size()]));
    }

    /**
     * 将正在加载的过程状态移除
     */
    private void removeLoadingItem(String loadingKey) {
        String[] loadingKeys = getArguments().getStringArray(KEY_LOADING_ARRAY);
        if (loadingKeys != null) {
            ArrayList<String> loadingKeyList = new ArrayList<>(Arrays.asList(loadingKeys));
            loadingKeyList.remove(loadingKey);
            getArguments().putStringArray(KEY_LOADING_ARRAY,
                    loadingKeyList.toArray(new String[loadingKeyList.size()]));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPaymentChanged()) {
            removePaymentChanged();
            checkOrderPay();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TO_PAY:
                if (isResumed()) {
                    checkOrderPay();
                } else {
                    setPaymentChanged();
                }
                break;
            case REQUEST_FILLIN_RIGHT:
                if (resultCode != Activity.RESULT_OK) {
                    showToast("没有查询到权限!");
                    fillInSwitch.setChecked(false);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean isPaymentChanged() {
        return getArguments().getBoolean("payment_changed");
    }

    private void setPaymentChanged() {
        getArguments().putBoolean("payment_changed", true);
    }

    private void removePaymentChanged() {
        getArguments().remove("payment_changed");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        paidTotalViewBinder = null;
        orderTotalViewBinder = null;
        paymentsListView = null;
        adapter = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
