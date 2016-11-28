package com.unioncloud.newpay.presentation.ui.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.raizlabs.coreutils.threading.ThreadingUtils;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.order.OrderStatistics;
import com.unioncloud.newpay.domain.model.order.QuerySaleOrderCommand;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.utils.DateFormatUtils;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.order.OrderStatisticsHandler;
import com.unioncloud.newpay.presentation.presenter.print.PrintStatisticsHandler;

import java.util.Date;

/**
 * Created by cwj on 16/9/2.
 */
public class OrderTodayCountFragment extends StatedFragment {

    public static OrderTodayCountFragment newInstance() {
        OrderTodayCountFragment fragment = new OrderTodayCountFragment();
        return fragment;
    }

    private static StateUpdateHandlerListener<OrderTodayCountFragment, OrderStatisticsHandler>
            statisticsHandlerListener =
            new StateUpdateHandlerListener<OrderTodayCountFragment, OrderStatisticsHandler>() {
                @Override
                public void onUpdate(String key, OrderTodayCountFragment handler, OrderStatisticsHandler response) {
                    handler.dealStatistics(response);
                }

                @Override
                public void onCleanup(String key, OrderTodayCountFragment handler, OrderStatisticsHandler response) {
                    response.removeCompletionListener(handler.statisticsListener);
                }
            };
    private UpdateCompleteCallback<OrderStatisticsHandler> statisticsListener =
            new UpdateCompleteCallback<OrderStatisticsHandler>() {
                @Override
                public void onCompleted(OrderStatisticsHandler response, boolean isSuccess) {
                    dealStatistics(response);
                }
            };

    private static StateUpdateHandlerListener<OrderTodayCountFragment, PrintStatisticsHandler> printHandlerListener =
            new StateUpdateHandlerListener<OrderTodayCountFragment, PrintStatisticsHandler>() {
                @Override
                public void onUpdate(String key, OrderTodayCountFragment handler, PrintStatisticsHandler response) {
                    handler.dealPrint(response);
                }

                @Override
                public void onCleanup(String key, OrderTodayCountFragment handler, PrintStatisticsHandler response) {
                    response.removeCompletionListener(handler.printListener);
                }
            };
    private UpdateCompleteCallback<PrintStatisticsHandler> printListener =
            new UpdateCompleteCallback<PrintStatisticsHandler>() {
                @Override
                public void onCompleted(PrintStatisticsHandler response, boolean isSuccess) {
                    dealPrint(response);
                }
            };

    OrderStatisticsBinder statisticsViewBinder;

    OrderStatistics statistics;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_today_count, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        statisticsViewBinder = new OrderStatisticsBinder(view);
        setTitle("交易日结");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestStatistics();
    }

    private void requestStatistics() {
        PosInfo posInfo = PosDataManager.getInstance().getPosInfo();
        QuerySaleOrderCommand command = new QuerySaleOrderCommand();
        command.setShopId(posInfo.getShopId());
        command.setPosId(posInfo.getPosId());
        String todayDate = DateFormatUtils.yyyy_MM_dd(new Date());
        command.setStartDate(todayDate);
        command.setEndDate(todayDate);

        OrderStatisticsHandler handler = new OrderStatisticsHandler(command);
        updateForResponse("OrderTodayCountFragment:statistics", handler, statisticsHandlerListener);
        handler.run();
    }

    private void dealStatistics(OrderStatisticsHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("正在统计...");
                handler.addCompletionListener(statisticsListener);
            } else {
                if (handler.isSuccess()) {
                    setStatisticsDate(handler.getData());
                } else {
                    showToast("获取统计数据失败");
                }
                dismissProgressDialog();
                cleanupResponse("OrderTodayCountFragment:statistics");
            }
        }
    }

    private void setStatisticsDate(final OrderStatistics statistics) {
        this.statistics = statistics;
        ThreadingUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                statisticsViewBinder.setOrderStatistics(statistics);
            }
        });
    }

    private void printStatistics() {
        if (statistics != null) {
            PrintStatisticsHandler handler = new PrintStatisticsHandler(getActivity(),
                    statistics, PosDataManager.getInstance().getPosInfo());
            updateForResponse("OrderTodayCountFragment:print", handler, printHandlerListener);
            handler.print();
        }
    }

    private void dealPrint(PrintStatisticsHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("打印统计数据...");
                handler.addCompletionListener(printListener);
            } else {
                if (handler.isSuccess() && handler.getData()) {
                    showToast("打印成功");
                } else {
                    showToast("打印失败");
                }
                dismissProgressDialog();
                cleanupResponse("OrderTodayCountFragment:print");
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.order_statistics, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.action_refresh:
                requestStatistics();
                return true;
            case R.id.action_print:
                printStatistics();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        statisticsViewBinder = null;
    }
}
