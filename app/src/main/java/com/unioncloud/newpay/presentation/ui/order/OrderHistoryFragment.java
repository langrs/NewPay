package com.unioncloud.newpay.presentation.ui.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.unioncloud.newpay.R;

import com.unioncloud.newpay.domain.model.order.QuerySaleOrderCommand;
import com.unioncloud.newpay.domain.model.order.SaleOrder;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.utils.DateFormatUtils;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.order.QuerySaleOrderHandler;
import com.unioncloud.newpay.presentation.ui.DatePickerDialog;

import java.util.Date;
import java.util.List;

/**
 * Created by cwj on 16/8/26.
 */
public class OrderHistoryFragment extends StatedFragment implements AdapterView.OnItemClickListener {

    public static OrderHistoryFragment newInstance() {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
        return fragment;
    }

    private static StateUpdateHandlerListener<OrderHistoryFragment, QuerySaleOrderHandler> queryHandlerListener =
            new StateUpdateHandlerListener<OrderHistoryFragment, QuerySaleOrderHandler>() {
                @Override
                public void onUpdate(String key, OrderHistoryFragment handler, QuerySaleOrderHandler response) {
                    handler.dealQuery(response);
                }

                @Override
                public void onCleanup(String key, OrderHistoryFragment handler, QuerySaleOrderHandler response) {
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

    private static final int REQUEST_CHANGE_START_DATE = 0X0001;
    private static final int REQUEST_CHANGE_END_DATE = 0X0002;

    private Button startDate;
    private Button endDate;
    private View emptyView;
    private ListView orderHistoryLv;
    private ImageButton queryBtn;

    private OrderHistoryHeaderBinder headerBinder;
    private OrderAdapter orderAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("交易查询");
        startDate = (Button) view.findViewById(R.id.fragment_order_history_start_date);
        endDate = (Button) view.findViewById(R.id.fragment_order_history_end_date);
        emptyView = view.findViewById(R.id.order_history_empty_container);
        orderHistoryLv = (ListView) view.findViewById(R.id.fragment_order_history_list);
        queryBtn = (ImageButton) view.findViewById(R.id.fragment_order_history_query_button);

        Date currentDate = new Date();
        startDate.setText(DateFormatUtils.yyyy_MM_dd(currentDate));
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStartDate();
            }
        });
        endDate.setText(DateFormatUtils.yyyy_MM_dd(currentDate));
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectEndDate();
            }
        });
        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryOrder();
            }
        });
        queryBtn.setVisibility(View.GONE);

        headerBinder = new OrderHistoryHeaderBinder();
        orderHistoryLv.addHeaderView(headerBinder.createView(getContext()));

        orderAdapter = new OrderAdapter(getContext());
        orderHistoryLv.setAdapter(orderAdapter);
        orderHistoryLv.setOnItemClickListener(this);
    }

    private void selectStartDate() {
        String startDateStr = startDate.getText().toString();
        Date startDate = DateFormatUtils.parseYYYY_MM_DD(startDateStr);

        DatePickerDialog dialog = DatePickerDialog.newInstance("选择开始日期", startDate, null);
        dialog.setTargetFragment(this, REQUEST_CHANGE_START_DATE);
        dialog.show(getChildFragmentManager(), "OrderHistoryFragment:selectStartDate");
    }

    private void selectEndDate() {
        String startDateStr = startDate.getText().toString();
        Date startDate = DateFormatUtils.parseYYYY_MM_DD(startDateStr);
        String endDateStr = endDate.getText().toString();
//        Date endDate = DateFormatUtils.parseYYYY_MM_DD(endDateStr);

        DatePickerDialog dialog = DatePickerDialog.newInstance("选择结束日期", startDate, startDate);
        dialog.setTargetFragment(this, REQUEST_CHANGE_END_DATE);
        dialog.show(getChildFragmentManager(), "OrderHistoryFragment:selectEndDate");
    }

    private void queryOrder() {
        PosInfo posInfo = PosDataManager.getInstance().getPosInfo();

        String startDateStr = startDate.getText().toString();
        String endDateStr = endDate.getText().toString();
        QuerySaleOrderCommand command = new QuerySaleOrderCommand();
        command.setShopId(posInfo.getShopId());
        command.setPosId(posInfo.getPosId());
        command.setStartDate(startDateStr);
        command.setEndDate(endDateStr);
        QuerySaleOrderHandler handler = new QuerySaleOrderHandler(command);
        updateForResponse("OrderHistoryFragment:query", handler, queryHandlerListener);
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
                cleanupResponse("OrderHistoryFragment:query");
                if (handler.isSuccess()) {
                    showToast("查询成功");
                    setOrderHistory(handler.getData());
                } else {
                    showToast("没有查询到该订单");
                    setOrderHistory(null);
                }
            }
        }
    }

    private void setOrderHistory(List<SaleOrder> orderHistory) {
        if (orderHistory == null || orderHistory.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            orderHistoryLv.setVisibility(View.GONE);
        } else {
            orderAdapter.setList(orderHistory);
            emptyView.setVisibility(View.GONE);
            orderHistoryLv.setVisibility(View.VISIBLE);
        }
        changeHeader(orderHistory);
    }

    private void changeHeader(List<SaleOrder> orderHistory) {
        int count = 0;
        int total = 0;
        if (orderHistory != null) {
            for (SaleOrder order : orderHistory) {
                count++;
                total += MoneyUtils.getFen(order.getHeader().getSaleAmount());
            }
        }
        headerBinder.setSum(count, total);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SaleOrder saleOrder = (SaleOrder) parent.getAdapter().getItem(position);
        toOrderDetail(saleOrder);
    }

    private void toOrderDetail(SaleOrder saleOrder) {
        Intent intent = OrderDetailActivity.getStartIntent(getActivity());
        intent.putExtra("order", saleOrder);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHANGE_START_DATE:
                if (resultCode == Activity.RESULT_OK) {
                    long milliseconds = data.getLongExtra("time", -1);
                    if (milliseconds != -1) {
                        startDate.setText(DateFormatUtils.yyyy_MM_dd(new Date(milliseconds)));
                    }
                }
                return;
            case REQUEST_CHANGE_END_DATE:
                if (resultCode == Activity.RESULT_OK) {
                    long milliseconds = data.getLongExtra("time", -1);
                    if (milliseconds != -1) {
                        endDate.setText(DateFormatUtils.yyyy_MM_dd(new Date(milliseconds)));
                    }
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.order_history, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.action_query:
                queryOrder();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        orderAdapter = null;
        headerBinder = null;
    }
}
