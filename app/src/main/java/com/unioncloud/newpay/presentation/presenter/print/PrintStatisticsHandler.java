package com.unioncloud.newpay.presentation.presenter.print;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esummer.android.updatehandler.UpdateHandler;
import com.pax.baselib.printer.Printer;
import com.pax.baselib.printer.PrinterThreadData;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.order.OrderStatistics;
import com.unioncloud.newpay.domain.model.order.OrderStatisticsPayment;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.utils.DateFormatUtils;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.executor.DefaultExecutorProvider;
import com.unioncloud.newpay.presentation.presenter.sharedpreferences.PreferencesUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by cwj on 16/9/4.
 */
public class PrintStatisticsHandler extends UpdateHandler<Boolean, PrintStatisticsHandler>
        implements Runnable {
    private Context context;
    private OrderStatistics statistics;
    private PosInfo posInfo;
    private String printTime;
    Printer printer = null;

    public PrintStatisticsHandler(Activity context, OrderStatistics statistics, PosInfo posInfo) {
        super(Boolean.FALSE, true);
        this.context = context;
        this.statistics = statistics;
        this.posInfo = posInfo;
        printTime = DateFormatUtils.allFriendlyFormat(new Date());
    }

    public void print() {
        DefaultExecutorProvider.getInstance().providerThread().execute(this);
    }

    @Override
    public void run() {
        printer = Printer.getInstance((Activity) context);
        printer.loadXml(R.layout.print_statistics);
        loadHeader();
        loadStatistics();
        int resultFlag = 0;
        int printCount = PreferencesUtils.getPrintCount(context);
        for (int i = 0; i < printCount; i++) {
            resultFlag += printer.startPrint();
        }
        data = (resultFlag == 0);
        onUpdateCompleted();

        printer = null;
    }

    private void loadHeader() {
        View headerView = ((Activity)this.context).getLayoutInflater().inflate(R.layout.print_statistics_header, null);
        TextView posNo = (TextView)headerView.findViewById(R.id.print_posno);
        TextView userNo = (TextView)headerView.findViewById(R.id.print_userno);
        TextView storeNo = (TextView)headerView.findViewById(R.id.print_store_name);
        TextView time = (TextView)headerView.findViewById(R.id.print_time);

        posNo.setText(posInfo.getPosNumber());
        userNo.setText(posInfo.getUserNumber());
        storeNo.setText(posInfo.getStoreName());
        time.setText(printTime);

        this.printer.setElement(R.id.print_statistics_header, headerView);
    }

    private void loadStatistics() {
        View statisticsView = ((Activity)this.context).getLayoutInflater().inflate(R.layout.print_statistics_payments, null);
        TextView saleCount = (TextView) statisticsView.findViewById(R.id.print_statistics_sale_count);
        TextView saleTotal = (TextView) statisticsView.findViewById(R.id.print_statistics_sale_total);
        TextView refundCount = (TextView) statisticsView.findViewById(R.id.print_statistics_refund_count);
        TextView refundTotal = (TextView) statisticsView.findViewById(R.id.print_statistics_refund_total);
        TextView orderTotal = (TextView) statisticsView.findViewById(R.id.print_statistics_total);
        LinearLayout payContainer = (LinearLayout) statisticsView.findViewById(R.id.print_statistics_payments_container);

        saleCount.setText(String.valueOf(statistics.getSaleCount()));
        saleTotal.setText(MoneyUtils.fenToString(statistics.getSaleTotals()));
        refundCount.setText(String.valueOf(statistics.getRefundCount()));
        refundTotal.setText(MoneyUtils.fenToString(statistics.getRefundTotal()));
        orderTotal.setText(MoneyUtils.fenToString(statistics.getStatisticsTotal()));

        List<OrderStatisticsPayment> payList = statistics.getPayStatistics();
        if (payList != null && payList.size() > 0) {
            for (OrderStatisticsPayment pay : payList) {
                View itemView = ((Activity)this.context).getLayoutInflater().inflate(R.layout.print_statistics_payment_item, null);
                ((TextView)itemView.findViewById(R.id.print_statistics_payment_name)).setText(pay.paymentName);
                ((TextView)itemView.findViewById(R.id.print_statistics_payment_total)).setText(MoneyUtils.fenToString(pay.payTotals));
                payContainer.addView(itemView);
            }
        }
        this.printer.setElement(R.id.print_statistics_container, statisticsView);
    }

}
