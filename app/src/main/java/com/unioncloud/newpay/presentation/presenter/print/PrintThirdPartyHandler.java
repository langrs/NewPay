package com.unioncloud.newpay.presentation.presenter.print;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.esummer.android.updatehandler.UpdateHandler;
import com.pax.baselib.printer.Printer;
import com.pax.baselib.printer.PrinterThreadData;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.print.PrintThirdPartyOrder;
import com.unioncloud.newpay.executor.DefaultExecutorProvider;
import com.unioncloud.newpay.presentation.presenter.sharedpreferences.PreferencesUtils;

/**
 * Created by cwj on 16/8/20.
 */
public class PrintThirdPartyHandler extends UpdateHandler<Boolean, PrintThirdPartyHandler>
        implements Runnable {

    PrintThirdPartyOrder printThirdPartyOrder;
    private Context context;

    public PrintThirdPartyHandler(Activity context, PrintThirdPartyOrder printThirdPartyOrder) {
        super(false, true);
        this.printThirdPartyOrder = printThirdPartyOrder;
        this.context = context;
    }

    public void print() {
        DefaultExecutorProvider.getInstance().providerThread().execute(this);
    }

    @Override
    public void run() {
        printThirdPartyOrder();
    }

    private void printThirdPartyOrder() {
        Printer printer = Printer.getInstance((Activity) context);
        printer.loadXml(R.layout.print_thirdparty_pay);
        PrinterThreadData printerThreadData = PrinterThreadData.getPrinterThreadData();

//        View view = ((Activity)this.context).getLayoutInflater().inflate(R.layout.print_thirdparty_pay, null);
        View view = printerThreadData.getRootView();
        TextView title = (TextView) view.findViewById(R.id.print_thirdparty_title);
        TextView shopName = (TextView) view.findViewById(R.id.print_shop_name);
        TextView storeName = (TextView) view.findViewById(R.id.print_store_name);
        TextView mchId = (TextView) view.findViewById(R.id.print_mch_id);
        TextView posNo = (TextView) view.findViewById(R.id.print_pos_no);
        TextView userNo = (TextView) view.findViewById(R.id.print_user_no);
        TextView tradeType = (TextView) view.findViewById(R.id.print_trade_type);
        TextView date = (TextView) view.findViewById(R.id.print_datetime);
        TextView thirdPartyOrderId = (TextView) view.findViewById(R.id.print_thirdparty_no);
        TextView amount = (TextView) view.findViewById(R.id.print_amount);

        title.setText(printThirdPartyOrder.getOrderTitle());
        shopName.setText(printThirdPartyOrder.getShopName());
        storeName.setText(printThirdPartyOrder.getStoreName());
        mchId.setText(printThirdPartyOrder.getMchId());
        posNo.setText(printThirdPartyOrder.getPosNo());
        userNo.setText(printThirdPartyOrder.getUserNo());
        tradeType.setText(printThirdPartyOrder.getTradeType());
        date.setText(printThirdPartyOrder.getDate());
        thirdPartyOrderId.setText(printThirdPartyOrder.getThirdPartyOrderId());
        amount.setText(printThirdPartyOrder.getAmount());

        int resultFlag = 0;
        int printCount = PreferencesUtils.getPrintCount(context);
        for (int i = 0; i < printCount; i++) {
            resultFlag += printer.startPrint();
        }
        data = (resultFlag == 0);
        onUpdateCompleted();
    }
}
