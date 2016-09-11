package com.unioncloud.newpay.presentation.ui.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.esummer.android.dialog.DefaultDialogBuilder;
import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.erp.VipCard;
import com.unioncloud.newpay.domain.model.order.SaleOrder;
import com.unioncloud.newpay.domain.model.order.SaleOrderHeader;
import com.unioncloud.newpay.domain.model.order.SaleOrderResult;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.model.print.PrintOrderInfo;
import com.unioncloud.newpay.domain.model.product.Product;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.presentation.model.cart.CartDataManager;
import com.unioncloud.newpay.presentation.model.checkout.CheckoutDataManager;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.model.refund.OriginalRefundInfo;
import com.unioncloud.newpay.presentation.model.refund.RefundDataManager;
import com.unioncloud.newpay.presentation.presenter.print.PrintOrderHandler;
import com.unioncloud.newpay.presentation.presenter.refund.RefundOrderHandler;
import com.unioncloud.newpay.presentation.ui.pay.PaymentSignpost;
import com.unioncloud.newpay.presentation.ui.refund.RefundActivity;
import com.unioncloud.newpay.presentation.ui.refund.RefundFragment;

import java.util.List;

/**
 * Created by cwj on 16/8/24.
 */
public class OrderDetailFragment extends StatedFragment {

    private static StateUpdateHandlerListener<OrderDetailFragment, RefundOrderHandler> refundHandlerListener =
            new StateUpdateHandlerListener<OrderDetailFragment, RefundOrderHandler>() {
                @Override
                public void onUpdate(String key, OrderDetailFragment handler, RefundOrderHandler response) {
                    handler.dealRefund(response);
                }

                @Override
                public void onCleanup(String key, OrderDetailFragment handler, RefundOrderHandler response) {
                    response.removeCompletionListener(handler.refundListener);
                }
            };
    private UpdateCompleteCallback<RefundOrderHandler> refundListener = new UpdateCompleteCallback<RefundOrderHandler>() {

        @Override
        public void onCompleted(RefundOrderHandler response, boolean isSuccess) {
            dealRefund(response);
        }
    };

    private static StateUpdateHandlerListener<OrderDetailFragment, PrintOrderHandler> printHandlerListener =
            new StateUpdateHandlerListener<OrderDetailFragment, PrintOrderHandler>() {
                @Override
                public void onUpdate(String key, OrderDetailFragment handler, PrintOrderHandler response) {
                    handler.dealPrint(response);
                }

                @Override
                public void onCleanup(String key, OrderDetailFragment handler, PrintOrderHandler response) {
                    response.removeCompletionListener(handler.printListener);
                }
            };
    private UpdateCompleteCallback<PrintOrderHandler> printListener = new UpdateCompleteCallback<PrintOrderHandler>() {
        @Override
        public void onCompleted(PrintOrderHandler response, boolean isSuccess) {
            dealPrint(response);
        }
    };

    private TextView orderNoTv;
    private TextView totalTv;
    private TextView paidTotalTv;
    private TextView originalPointsTv;
    private TextView pointsTv;
    private TextView orderTypeTv;
    private TextView dateTimeTv;

//    private ListView productListView;
//    private ListView paidListView;
    private LinearLayout productLayout;
    private LinearLayout paidLayout;


    private Button okButton;

    private SaleOrder saleOrder;

    public static OrderDetailFragment newIntance(SaleOrder saleOrder) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.getArguments().putSerializable("order", saleOrder);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle("交易详情");

        orderNoTv = (TextView) view.findViewById(R.id.order_detail_saleNo);
        totalTv = (TextView) view.findViewById(R.id.order_detail_need_payAmount);
        paidTotalTv = (TextView) view.findViewById(R.id.order_detail_payAmount);
        originalPointsTv = (TextView) view.findViewById(R.id.order_detail_pointsSrc);
        pointsTv = (TextView) view.findViewById(R.id.order_detail_points);
        orderTypeTv = (TextView) view.findViewById(R.id.order_detail_saleType);
        dateTimeTv = (TextView) view.findViewById(R.id.order_detail_time);

        productLayout = (LinearLayout) view.findViewById(R.id.fragment_refund_detail_itemList_container);
        paidLayout = (LinearLayout) view.findViewById(R.id.fragment_refund_detail_paidList_container);

        okButton = (Button) view.findViewById(R.id.fragment_refund_detail_ok_btn);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRefundPaid();
            }
        });
        initData();
    }

    private void initData() {
        SaleOrder order = (SaleOrder) getArguments().getSerializable("order");
        saleOrder = order;
        if (order != null) {
            orderNoTv.setText(order.getHeader().getSaleNumber());
            String totalAmount = order.getHeader().getSaleAmount();
            totalTv.setText(MoneyUtils.fenToString(MoneyUtils.getFen(totalAmount)));    // 返回的是整数是需要转换
            String paidTotal = order.getHeader().getPayAmount();
            paidTotalTv.setText(MoneyUtils.fenToString(MoneyUtils.getFen(paidTotal)));
            originalPointsTv.setText(order.getHeader().getPreviousPoints());
            pointsTv.setText(order.getHeader().getSalePoints());
            dateTimeTv.setText(order.getHeader().getSaleDate());
            String orderTypeFlag = order.getHeader().getDealType();
            String orderType = null;
            if ("01".equals(orderTypeFlag)) {
                orderType = "销售";
            } else if ("02".equals(orderTypeFlag)){
                orderType = "退货";
                dissmissRefundAction();
            }
            orderTypeTv.setText(orderType);

            List<CartItem> itemList = order.getItemList();
            if (itemList != null) {
                for (CartItem cartItem : itemList) {
                    View orderItemView = LayoutInflater.from(getActivity())
                            .inflate(R.layout.list_item_order_product, productLayout, false);
                    TextView productName = (TextView) orderItemView.findViewById(R.id.list_item_order_product_name);
                    TextView productNumber = (TextView) orderItemView.findViewById(R.id.list_item_order_product_number);
                    TextView productPrice = (TextView) orderItemView.findViewById(R.id.list_item_order_product_price);
                    TextView productQuantity = (TextView) orderItemView.findViewById(R.id.list_item_order_product_quantity);
                    productName.setText(cartItem.getProductName());
                    productNumber.setText(cartItem.getProductNumber());
                    productPrice.setText(String.format("¥ %1s", MoneyUtils.fenToString(cartItem.getSellUnitPrice())));
                    productQuantity.setText(String.format("数量%1$6d", cartItem.getQuantity()));
                    productLayout.addView(orderItemView);
                }
            }

            List<PaymentUsed> paidList = order.getPaymentUsedList();
            if (paidList != null) {
                for (PaymentUsed used : paidList) {
                    Payment payment = PosDataManager.getInstance().getPaymentById(used.getPaymentId());

                    View paidView = LayoutInflater.from(getActivity()).
                            inflate(R.layout.list_item_paid_info, paidLayout ,false);
                    TextView name = (TextView) paidView.findViewById(R.id.list_item_paid_info_payment_name);
                    TextView amount = (TextView) paidView.findViewById(R.id.list_item_paid_info_payment_amount);
                    TextView billNo = (TextView) paidView.findViewById(R.id.list_item_paid_info_billNo);

                    name.setText(payment.getPaymentName());
                    amount.setText(MoneyUtils.fenToString(used.getPayAmount()));
                    if (TextUtils.isEmpty(used.getRelationNumber())) {
                        billNo.setVisibility(View.GONE);
                    } else {
                        billNo.setText(used.getRelationNumber());
                    }
                    paidLayout.addView(paidView);
                }
            }
//            productListView.setAdapter(new OrderProductAdapter(getActivity(), order.getItemList()));
//            paidListView.setAdapter(new OrderPaidAdapter(getActivity(), order.getPaymentUsedList()));
        }
    }

    private void toShowRefundPaid(PaymentUsed paymentUsed) {
        PaymentSignpost signpost = PaymentSignpost.getSignpost(paymentUsed.getPaymentId());
        if (signpost != null) {
            boolean supportRefund = signpost.supportRefund();
            if (supportRefund) {
                OriginalRefundInfo info = new OriginalRefundInfo();
                info.setOriginalBillNo(paymentUsed.getRelationNumber());
                info.setRefundAmount(paymentUsed.getPayAmount());
                info.setOriginalOrderId(saleOrder.getHeader().getSaleNumber());
                Intent intent = RefundActivity.getOriginalRefundIntent(getActivity(), signpost, info);
                startActivityForResult(intent, getRefundPaidRequestCode());
            } else {
                showToast("暂不支持");
            }
        }
    }

    private int currentRefundPaidIndex;

    private int getRefundPaidRequestCode() {
        return currentRefundPaidIndex + 100;
    }

    private int getPaidIndex(int refundRequestCode) {
        return refundRequestCode - 100;
    }


    private void refundOrder() {
        RefundDataManager manager = RefundDataManager.getInstance();
        String refundOrderId = manager.createOrderId();
        RefundOrderHandler handler = new RefundOrderHandler(PosDataManager.getInstance().getPosInfo(),
                refundOrderId, saleOrder, manager.getPaidPayments());
        updateForResponse("OrderDetailFragment:refundOrder", handler, refundHandlerListener);
        handler.run();

    }

    private void dealRefund(RefundOrderHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("提交退货单...");
                handler.addCompletionListener(refundListener);
            } else {
                if (handler.isSuccess()) {
                    toPrintRefundOrder(handler.getData());
                } else {
                    dismissProgressDialog();
                    showRetryRefundOrder();
                    showToast("退货失败!");
                }
                cleanupResponse("OrderDetailFragment:refundOrder");
            }
        }
    }

    private static final int REQUEST_RETRY_REFUND_ORDER = 0x222;

    private void showRetryRefundOrder() {
        DefaultDialogBuilder builder = createAndSaveDialogBuilder(REQUEST_RETRY_REFUND_ORDER);
        builder.setTitle("提交退货单失败");
        builder.setMessage("请确定再次提交,还是取消退货单");
        builder.setCancelable(false);
        showDialog(builder);
    }

    private void toPrintRefundOrder(SaleOrderResult saleOrderResult) {

        PrintOrderInfo info = new PrintOrderInfo();
        info.setPosInfo(PosDataManager.getInstance().getPosInfo());
        info.setOrderId(saleOrderResult.getSaleNo());
        info.setSaleTime(saleOrderResult.getSaleDate());
        info.setCartItemList(RefundDataManager.getInstance().getItemList());
        if (!TextUtils.isEmpty(saleOrder.getHeader().getVipCardNumber())) {
            info.setVipNo(saleOrder.getHeader().getVipCardNumber());
        }
        info.setSalePoints(saleOrderResult.getSalePoints());
        info.setOriginalPoints(saleOrderResult.getOriginalPoints());
        info.setFooter(PosDataManager.getInstance().getPosInfo().getPrintFooter());
        info.setPaymentUsedList(RefundDataManager.getInstance().getPaidPayments());
        info.setTotal("-" + saleOrder.getHeader().getOriginalAmount());
        info.setPaidTotal("-" + saleOrder.getHeader().getSaleAmount());
        if (!TextUtils.isEmpty(saleOrder.getHeader().getDiscountAmount())
                && Float.valueOf(saleOrder.getHeader().getDiscountAmount()) != 0) {
            info.setDiscountTotal("-" + saleOrder.getHeader().getDiscountAmount());
        }
        PrintOrderHandler handler = new PrintOrderHandler(getActivity(), info);
        handler.setPrintTag("正在打印退货单...");
        updateForResponse("OrderDetailFragment:PrintOrder", handler, printHandlerListener);
        handler.print();
    }

    private void reprint() {
        SaleOrderHeader header = saleOrder.getHeader();

        PrintOrderInfo info = new PrintOrderInfo();
        info.setReprint(true);
        info.setPosInfo(PosDataManager.getInstance().getPosInfo());
        info.setOrderId(header.getSaleNumber());
        info.setSaleTime(header.getSaleDate());

        PosDataManager posDataManager = PosDataManager.getInstance();
        for (CartItem cartItem : saleOrder.getItemList()) {
            Product product = posDataManager.getLocalProductById(cartItem.getProductId());
            cartItem.setProductName(product.getProductName());
        }
        info.setCartItemList(saleOrder.getItemList());
        if (!TextUtils.isEmpty(header.getVipCardNumber())) {
            info.setVipNo(header.getVipCardNumber());
        }
        info.setSalePoints(header.getSalePoints());
        info.setOriginalPoints(header.getPreviousPoints());
        info.setFooter(PosDataManager.getInstance().getPosInfo().getPrintFooter());
        info.setPaymentUsedList(saleOrder.getPaymentUsedList());
        info.setTotal(header.getOriginalAmount());
        info.setPaidTotal(header.getSaleAmount());
        if (!TextUtils.isEmpty(header.getDiscountAmount())
                && Float.valueOf(header.getDiscountAmount()) != 0) {
            info.setDiscountTotal(header.getDiscountAmount());
        }
        PrintOrderHandler handler = new PrintOrderHandler(getActivity(), info);
        handler.setPrintTag("正在重打印小票...");
        updateForResponse("OrderDetailFragment:PrintOrder", handler, printHandlerListener);
        handler.print();
    }

    private void dealPrint(PrintOrderHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog(handler.getPrintTag());
                handler.addCompletionListener(printListener);
            } else {
                dismissProgressDialog();
                cleanupResponse("OrderDetailFragment:PrintOrder");
                if (handler.isSuccess() && handler.getData()) {
                    showToast("打印完成");
                } else {
                    showToast("打印失败");
                }
                RefundDataManager.getInstance().clear();
                getActivity().finish();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefundChanged()) {
            removeRefundChanged();
            checkRefundPaid();
        }
    }

    private void checkRefundPaid() {
        RefundDataManager.getInstance().createOrderId();

        List<PaymentUsed> paidList = saleOrder.getPaymentUsedList();
        if (currentRefundPaidIndex < paidList.size()) {
            toShowRefundPaid(paidList.get(currentRefundPaidIndex));
        } else {
            refundOrder();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == getRefundPaidRequestCode()) {
            switch (resultCode) {
                case Activity.RESULT_OK:
//                    currentRefundPaidIndex++;
//                    checkRefundPaid();
                    setRefundChanged();
                    break;
                case Activity.RESULT_CANCELED:
                    showToast("退款失败");
                    break;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setRefundChanged() {
        currentRefundPaidIndex++;
        getArguments().putBoolean("refundChanged", true);
    }

    private boolean isRefundChanged() {
        return getArguments().getBoolean("refundChanged");
    }

    private void removeRefundChanged() {
        getArguments().remove("refundChanged");
    }

    @Override
    protected void onDialogOkClicked(int dialogId) {
        if (dialogId == REQUEST_RETRY_REFUND_ORDER) {
            dismissDialog(getSavedBuilder(dialogId));
            refundOrder();
        }
    }

    @Override
    protected void onDialogCancelClicked(int dialogId) {
        if (dialogId == REQUEST_RETRY_REFUND_ORDER) {
            dismissDialog(getSavedBuilder(dialogId));
            CheckoutDataManager.getInstance().clear();
            getActivity().finish();
        }
    }

    private void dissmissRefundAction() {
        getActivity().supportInvalidateOptionsMenu();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (saleOrder != null && "02".equals(saleOrder.getHeader().getDealType())) {
            menu.findItem(R.id.action_refund).setVisible(false);
        } else {
            menu.findItem(R.id.action_refund).setVisible(true);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.order_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                RefundDataManager.getInstance().clear();
                getActivity().finish();
                return true;
            case R.id.action_refund:
                checkRefundPaid();
                return true;
            case R.id.action_reprint:
                reprint();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean isRefunding() {
        if (currentRefundPaidIndex != 0 && RefundDataManager.getInstance().getPaidPayments().size()> 1) {
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saleOrder = null;
    }
}
