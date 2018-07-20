package com.unioncloud.newpay.presentation.presenter.print;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pax.api.PrintException;
import com.pax.baselib.printer.DevPrinter;
import com.pax.baselib.printer.Printer;
import com.pax.baselib.printer.PrinterThreadData;
import com.raizlabs.coreutils.view.ViewUtils;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.erp.Coupon;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.print.PrintOrderInfo;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.executor.DefaultExecutorProvider;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.sharedpreferences.PreferencesUtils;
import com.unioncloud.newpay.presentation.ui.pay.PaymentSignpost;
import com.unioncloud.newpay.presentation.utils.QrUtils;

import java.util.List;

/**
 * 不再通过Activity打印图片
 */
public class PrintOrderHandlerNew extends PrintOrderHandler {
    public static final String CHARSET = "UTF-8";

    private PrintOrderInfo printOrderInfo;

    Printer printer = null;
    private Context context;

    private String printTag;

    DevPrinter devPrinter;

    public PrintOrderHandlerNew(Activity context, PrintOrderInfo printOrderInfo) {
        super(context, printOrderInfo);

        this.printOrderInfo = printOrderInfo;
        this.context = context;

        devPrinter = DevPrinter.getInstance();
    }

    public String getPrintTag() {
        return printTag;
    }

    public void setPrintTag(String printTag) {
        this.printTag = printTag;
    }

    public void print() {
        DefaultExecutorProvider.getInstance().providerThread().execute(this);
    }

    @Override
    public void run() {
//        this.printer = Printer.getInstance((Activity) context);
//        boolean printResult = printOrder();
//        if (printResult && !printOrderInfo.isReprint()) {
//            printResult &= printCoupon();
//        }
        try {
            devPrinter.init();
            int printCount = PreferencesUtils.getPrintCount(context);
            for (int i = 0; i < printCount; i++) {
                printOrder();
            }
            if (!printOrderInfo.isReprint()) {
                printCoupon();
            }
            devPrinter.start();
            byte status = waitFinishStatus();
            // status 为0表示打印成功
            data = (status == 0);
        } catch (PrintException e) {
            Log.e("Print", "errorCode = " + e.exceptionCode);
            data = false;
        }
        onUpdateCompleted();
    }

    private void printOrder() throws PrintException {
        loadHeader();
        loadPos();
        loadProducts();
        loadPay();
        loadVip();
        loadFooter();
    }

    private void loadHeader() throws PrintException {
        devPrinter.printStr("合胜百货", CHARSET);
    }

    private void loadPos() throws PrintException {
        PosInfo posInfo = printOrderInfo.getPosInfo();
        if (posInfo != null) {
            devPrinter.printStr(posInfo.getPosNumber(), CHARSET);
            devPrinter.printStr(posInfo.getUserNumber(), CHARSET);
        }
        devPrinter.printStr(printOrderInfo.getSaleTime(), CHARSET);
        devPrinter.printStr(printOrderInfo.getOrderId(), CHARSET);
        if (printOrderInfo.isReprint()) {
            devPrinter.printStr("-----------------重打印!-------------------", CHARSET);
        } else {
            devPrinter.printStr("----------------------------------", CHARSET);
        }
    }

    private void loadProducts() throws PrintException {
        List<CartItem> cartItems = printOrderInfo.getCartItemList();
        printProductColumnHeader();
        if (cartItems != null && cartItems.size() > 0) {
            for (CartItem cartItem : cartItems) {
                devPrinter.printStr(cartItem.getProductNumber()
                        + "\t" + cartItem.getProductName(), CHARSET);
                devPrinter.printStr(Integer.toString(cartItem.getRowNumber()), CHARSET);
                devPrinter.printStr(Integer.toString(cartItem.getQuantity()), CHARSET);
                devPrinter.printStr(MoneyUtils.fenToString(cartItem.getSellUnitPrice()), CHARSET);
                devPrinter.printStr(MoneyUtils.fenToString(cartItem.subtotal()), CHARSET);
            }
        }
    }

    /**
     * 打印商品项的竖列的头部
     */
    private void printProductColumnHeader() throws PrintException {
        // TODO: 1/3/17
    }

    private void loadPay() throws PrintException {
        devPrinter.printStr("应付: " + printOrderInfo.getTotal(), CHARSET);
        devPrinter.printStr("实付: " + printOrderInfo.getPaidTotal(), CHARSET);
        if (!TextUtils.isEmpty(printOrderInfo.getDiscountTotal())) {
            devPrinter.printStr("折扣:  " + printOrderInfo.getDiscountTotal(), CHARSET);
        }

        List<PaymentUsed> paymentUseds = printOrderInfo.getPaymentUsedList();
        devPrinter.printStr("付款:", CHARSET);
        if (paymentUseds != null && paymentUseds.size() > 0) {
            for (PaymentUsed used : paymentUseds) {
                Payment payment = PosDataManager.getInstance().getPaymentById(used.getPaymentId());
                devPrinter.printStr(payment.getPaymentName(), CHARSET);
                StringBuilder sb = new StringBuilder();
                int changeAmount = used.getChangeAmount();
                if (changeAmount > 0) {
                    int allPay = used.getPayAmount() + changeAmount;
                    sb.append(MoneyUtils.fenToString(allPay)).append("   找零").append(MoneyUtils.fenToString(changeAmount));
                } else {
                    sb.append(MoneyUtils.fenToString(used.getPayAmount()));
                }
                devPrinter.printStr(sb.toString(), CHARSET);

                boolean needPrintBillNo = PaymentSignpost.needPrintBillNo(payment);
                if (needPrintBillNo) {
                    devPrinter.printStr("号码  " + used.getRelationNumber(), CHARSET);
                }
            }
        }
    }

    private void loadVip() throws PrintException {
        String vipNo = printOrderInfo.getVipNo();
        if (!TextUtils.isEmpty(vipNo)) {
            devPrinter.printStr("会员: " + vipNo, CHARSET);
            devPrinter.printStr("本次积分: " + printOrderInfo.getSalePoints(), CHARSET);
            devPrinter.printStr("上期积分: " + printOrderInfo.getOriginalPoints(), CHARSET);
        }
    }

    private void loadFooter() throws PrintException {
        devPrinter.printStr("----------------------------------", CHARSET);
        devPrinter.printStr("欢迎再次光临", CHARSET);
        devPrinter.printStr("如需发票请在一个月内凭小票办理", CHARSET);
//        汕头热线电话
//        devPrinter.printStr("服务热线:0754-81857777", CHARSET);
//        坦洲热线电话
        devPrinter.printStr("服务热线:0760-86788889", CHARSET);
    }

    private void printCoupon() throws PrintException {
        List<Coupon> coupons = printOrderInfo.getCoupons();
        if (coupons == null || coupons.size() == 0) {
            return;
        }
        devPrinter.printStr("合胜百货购物券", CHARSET);
        for (int i = 0, size = coupons.size(); i < size; i++) {
            Coupon coupon = coupons.get(i);
            devPrinter.printStr("券号: " + coupon.getCouponNo(), CHARSET);
            devPrinter.printStr("金额: " + coupon.getCouponValue(), CHARSET);
            devPrinter.printStr("截止日期: " + coupon.getEndDate(), CHARSET);
            int pix = 300;
            devPrinter.printBitMap(QrUtils.createQRImage(coupon.getCouponNo(), pix, pix));
        }
    }

    private byte waitFinishStatus() throws PrintException{
        byte status;
        while(true) {
            status = devPrinter.getStatus();
            // 如果状态不是正在打印, 返回状态值.
            if(status != 1) {
                return status;
            }
            SystemClock.sleep(200L);
        }
    }
}
