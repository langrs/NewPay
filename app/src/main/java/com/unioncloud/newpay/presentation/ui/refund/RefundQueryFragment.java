package com.unioncloud.newpay.presentation.ui.refund;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.order.QuerySaleOrderCommand;
import com.unioncloud.newpay.domain.model.order.SaleOrder;
import com.unioncloud.newpay.presentation.presenter.order.QuerySaleOrderHandler;
import com.unioncloud.newpay.presentation.ui.order.OrderDetailActivity;

/**
 * Created by cwj on 16/8/24.
 */
public class RefundQueryFragment extends StatedFragment {


    public static RefundQueryFragment getInstance() {
        RefundQueryFragment fragment = new RefundQueryFragment();
        return fragment;
    }

    private static StateUpdateHandlerListener<RefundQueryFragment, QuerySaleOrderHandler> queryHandlerListener =
            new StateUpdateHandlerListener<RefundQueryFragment, QuerySaleOrderHandler>() {
                @Override
                public void onUpdate(String key, RefundQueryFragment handler, QuerySaleOrderHandler response) {
                    handler.dealQuery(response);
                }

                @Override
                public void onCleanup(String key, RefundQueryFragment handler, QuerySaleOrderHandler response) {
                    response.removeCompletionListener(handler.queryListener);
                }
            };
    private UpdateCompleteCallback<QuerySaleOrderHandler> queryListener =
            new UpdateCompleteCallback<QuerySaleOrderHandler>() {
                @Override
                public void onCompleted(QuerySaleOrderHandler response, boolean isSuccess) {
                    dealQuery(response);
                }
            };

    private EditText orderNumberEt;
    private Button okButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refund_query, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("退货查询");
        orderNumberEt = (EditText) view.findViewById(R.id.fragment_refund_query_input);
        okButton = (Button) view.findViewById(R.id.fragment_refund_query_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryOrder();
            }
        });
    }

    private void queryOrder() {
        String orderNo = orderNumberEt.getText().toString();
        if (TextUtils.isEmpty(orderNo)) {
            showToast("交易单号不能为空");
            return;
        }
        QuerySaleOrderCommand command = new QuerySaleOrderCommand();
        command.setSaleNo(orderNo);
        QuerySaleOrderHandler handler = new QuerySaleOrderHandler(command);
        updateForResponse("RefundQueryFragment:query", handler, queryHandlerListener);
        handler.run();
    }

    private void dealQuery(QuerySaleOrderHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog();
                handler.addCompletionListener(queryListener);
            } else {
                dismissProgressDialog();
                cleanupResponse("RefundQueryFragment:query");
                if (handler.isSuccess() &&
                        handler.getData() != null &&
                        handler.getData().size() == 1) {
                    toRefundOrderDetail(handler.getData().get(0));
                } else {
                    showToast("没有查询到该订单");
                }
            }
        }
    }

    private void toRefundOrderDetail(SaleOrder order) {
        Intent intent = OrderDetailActivity.getStartIntent(getActivity());
        intent.putExtra("order", order);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        orderNumberEt = null;
        okButton = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
