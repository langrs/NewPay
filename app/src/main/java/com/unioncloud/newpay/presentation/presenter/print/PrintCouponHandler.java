package com.unioncloud.newpay.presentation.presenter.print;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.esummer.android.updatehandler.UpdateHandler;
import com.pax.baselib.printer.Printer;
import com.pax.baselib.printer.PrinterThreadData;
import com.raizlabs.coreutils.view.ViewUtils;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.executor.DefaultExecutorProvider;
import com.unioncloud.newpay.presentation.utils.QrUtils;

/**
 * Created by cwj on 16/9/13.
 */
public class PrintCouponHandler extends UpdateHandler<Boolean, PrintCouponHandler>
        implements Runnable {

    private Context context;
    private Coupon coupon;

    public PrintCouponHandler(Activity context, Coupon coupon) {
        super(Boolean.FALSE, true);
        this.context = context;
        this.coupon = coupon;
    }

    public void print() {
        DefaultExecutorProvider.getInstance().providerThread().execute(this);
    }

    @Override
    public void run() {
        data = printCoupon();
        onUpdateCompleted();
    }

    private boolean printCoupon() {
        Printer printer = Printer.getInstance((Activity) context);
        printer.loadXml(R.layout.print_coupon);
        PrinterThreadData printerThreadData = PrinterThreadData.getPrinterThreadData();
        View view = printerThreadData.getRootView();
        TextView couponTitle = (TextView) view.findViewById(R.id.print_coupon_title);
        TextView couponNo = (TextView) view.findViewById(R.id.print_coupon_no);
        TextView couponValue = (TextView) view.findViewById(R.id.print_coupon_value);
        TextView couponEndDate = (TextView) view.findViewById(R.id.print_coupon_endDate);
        ImageView couponQr = (ImageView) view.findViewById(R.id.print_coupon_qr);

        String couponType = coupon.getFlag();
        if ("1".equals(couponType)) {
            couponTitle.setText("合胜百货积分返利券");
        } else if ("2".equals(couponType)) {
            couponTitle.setText("合胜百货折扣券");
        }
        couponNo.setText(coupon.getCouponNo());
        couponValue.setText(coupon.getCouponValue());
        couponEndDate.setText(coupon.getEndDate());
        int pix = (int) ViewUtils.dipsToPixels(300, couponQr);
        couponQr.setImageBitmap(QrUtils.createQRImage(coupon.getCouponNo(), pix, pix));
        int resultFlag = printer.startPrint();
        return (resultFlag == 0);
    }
}
