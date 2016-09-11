package com.unioncloud.newpay.presentation.presenter.print;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esummer.android.updatehandler.UpdateHandler;
import com.pax.baselib.printer.Printer;
import com.pax.baselib.printer.PrinterThreadData;
import com.raizlabs.coreutils.view.ViewUtils;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.model.erp.VipCard;
import com.unioncloud.newpay.domain.model.order.Coupon;
import com.unioncloud.newpay.domain.model.order.SaleOrderResult;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.print.PrintOrderInfo;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.executor.DefaultExecutorProvider;
import com.unioncloud.newpay.presentation.model.pos.PosDataManager;
import com.unioncloud.newpay.presentation.presenter.PresenterUtils;
import com.unioncloud.newpay.presentation.presenter.sharedpreferences.PreferencesUtils;
import com.unioncloud.newpay.presentation.ui.pay.PaymentSignpost;
import com.unioncloud.newpay.presentation.utils.QrUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 16/8/19.
 */
public class PrintOrderHandler extends UpdateHandler<Boolean, PrintOrderHandler> implements Runnable {
    private PrintOrderInfo printOrderInfo;

    Printer printer = null;
    private Context context;

    private String printTag;

    public PrintOrderHandler(Activity context, PrintOrderInfo printOrderInfo) {
        super(false, true);

        this.printOrderInfo = printOrderInfo;
        this.context = context;
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
        this.printer = Printer.getInstance((Activity) context);
        boolean printResult = printOrder();
        if (printResult && !printOrderInfo.isReprint()) {
            printResult &= printCoupon();
        }
        data = printResult;
        onUpdateCompleted();
        printer = null;
    }

    private boolean printOrder() {
        printer.loadXml(R.layout.print_order);
        loadHeader();
        loadPos();
        loadProducts();
        loadPay();
        loadVip();
        loadFooter();
        int resultFlag = 0;
        int printCount = PreferencesUtils.getPrintCount(context);
        for (int i = 0; i < printCount; i++) {
            resultFlag += printer.startPrint();
        }
        return (resultFlag == 0);
    }

    private void loadHeader() {
        View headerView = ((Activity)this.context).getLayoutInflater().inflate(R.layout.print_order_header, null);
        ((TextView)headerView.findViewById(R.id.print_header)).setText("合胜百货");
        this.printer.setElement(R.id.print_order_header, headerView);
    }

    private void loadPos() {
        View posView = ((Activity)this.context).getLayoutInflater().inflate(R.layout.print_order_pos, null);
        TextView posNo = (TextView)posView.findViewById(R.id.print_posno);
        TextView userNo = (TextView)posView.findViewById(R.id.print_userno);
        TextView orderTime = (TextView)posView.findViewById(R.id.print_time);
        TextView orderId = (TextView)posView.findViewById(R.id.print_orderid);
        TextView reprintTag = (TextView)posView.findViewById(R.id.print_reprint_tag);
        TextView lineTag = (TextView)posView.findViewById(R.id.print_line_tag);
        PosInfo posInfo = printOrderInfo.getPosInfo();
        if (posInfo != null) {
            posNo.setText(posInfo.getPosNumber());
            userNo.setText(posInfo.getUserNumber());
        }
        orderTime.setText(printOrderInfo.getSaleTime());
        orderId.setText(printOrderInfo.getOrderId());
        if (printOrderInfo.isReprint()) {
            reprintTag.setVisibility(View.VISIBLE);
            lineTag.setVisibility(View.GONE);
        } else {
            lineTag.setVisibility(View.VISIBLE);
            reprintTag.setVisibility(View.GONE);
        }
        this.printer.setElement(R.id.print_order_merchant, posView);
    }

    private void loadProducts() {
        LinearLayout productsView = (LinearLayout)((Activity)this.context).getLayoutInflater().inflate(R.layout.print_order_products, null);
        List<CartItem> cartItems = printOrderInfo.getCartItemList();
        if (cartItems != null && cartItems.size() > 0) {
            for (CartItem cartItem : cartItems) {
                View itemView = ((Activity)this.context).getLayoutInflater().inflate(R.layout.print_order_product_item, null);
                ((TextView) itemView.findViewById(R.id.print_order_product_name)).setText(cartItem.getProductNumber()
                        + "\t" + cartItem.getProductName());
                ((TextView) itemView.findViewById(R.id.print_order_product_rownumber)).setText(
                        String.valueOf(cartItem.getRowNumber()));
                ((TextView) itemView.findViewById(R.id.print_order_product_quantity)).setText(
                        String.valueOf(cartItem.getQuantity()));
                ((TextView) itemView.findViewById(R.id.print_order_product_unitprice)).setText(
                        MoneyUtils.fenToString(cartItem.getSellUnitPrice()));
                ((TextView) itemView.findViewById(R.id.print_order_product_subtotal)).setText(
                        MoneyUtils.fenToString(cartItem.getSellUnitPrice() * cartItem.getQuantity()));
                productsView.addView(itemView);
            }
            this.printer.setElement(R.id.print_order_products, productsView);
        }
    }

    private void loadPay() {
        LinearLayout paysView = (LinearLayout)((Activity)this.context).getLayoutInflater().
                inflate(R.layout.print_order_pays, null);
        TextView subTotal = (TextView) paysView.findViewById(R.id.print_pay_total);
        TextView payTotal = (TextView) paysView.findViewById(R.id.print_paid_total);
        View discountView = paysView.findViewById(R.id.print_discount_total_container);
        TextView discountTotal = (TextView) paysView.findViewById(R.id.print_discount_total);

        subTotal.setText(printOrderInfo.getTotal());
        payTotal.setText(printOrderInfo.getPaidTotal());
        if (TextUtils.isEmpty(printOrderInfo.getDiscountTotal())) {
            discountView.setVisibility(View.GONE);
        } else {
            discountView.setVisibility(View.VISIBLE);
            discountTotal.setText(printOrderInfo.getDiscountTotal());
        }

        List<PaymentUsed> paymentUseds = printOrderInfo.getPaymentUsedList();
        if (paymentUseds != null && paymentUseds.size() > 0) {
            for (PaymentUsed used : paymentUseds) {
                Payment payment = PosDataManager.getInstance().getPaymentById(used.getPaymentId());
                View itemView = ((Activity)this.context).getLayoutInflater().inflate(R.layout.print_order_pay_item, null);
                ((TextView)itemView.findViewById(R.id.print_order_pay_name)).setText(payment.getPaymentName());
                ((TextView)itemView.findViewById(R.id.print_order_pay_amount)).setText(MoneyUtils.fenToString(used.getPayAmount()));

                boolean needPrintBillNo = PaymentSignpost.needPrintBillNo(payment);
                if (needPrintBillNo) {
                    View billNoContainer = itemView.findViewById(R.id.print_order_pay_billNo_container);
                    ((TextView)billNoContainer.findViewById(R.id.print_order_pay_billNo)).setText(used.getRelationNumber());
                    billNoContainer.setVisibility(View.VISIBLE);
                }
                paysView.addView(itemView);
            }
            this.printer.setElement(R.id.print_order_pays, paysView);
        }
    }

    private void loadVip() {
        String vipNo = printOrderInfo.getVipNo();
        if (!TextUtils.isEmpty(vipNo)) {
            View vipView = ((Activity)this.context).getLayoutInflater().
                    inflate(R.layout.print_order_vip, null);
            TextView vipNoTv = (TextView) vipView.findViewById(R.id.print_order_vipno);
            TextView pointsTv = (TextView) vipView.findViewById(R.id.print_order_vip_points);
            TextView originalPointsTv = (TextView) vipView.findViewById(R.id.print_order_vip_original_points);
            vipNoTv.setText(vipNo);
            pointsTv.setText(printOrderInfo.getSalePoints());
            originalPointsTv.setText(printOrderInfo.getOriginalPoints());
            this.printer.setElement(R.id.print_order_vip, vipView);
        }
    }

    private void loadFooter() {
        View footerView = ((Activity)this.context).getLayoutInflater().
                inflate(R.layout.print_order_footer, null);
        this.printer.setElement(R.id.print_order_footer, footerView);
    }

    private boolean printCoupon() {
        List<Coupon> coupons = printOrderInfo.getCoupons();
        if (coupons == null || coupons.size() == 0) {
            return true;
        }
        printer.loadXml(R.layout.print_coupon);
        PrinterThreadData printerThreadData = PrinterThreadData.getPrinterThreadData();
        View view = printerThreadData.getRootView();
        TextView couponNo = (TextView) view.findViewById(R.id.print_coupon_no);
        TextView couponValue = (TextView) view.findViewById(R.id.print_coupon_value);
        TextView couponEndDate = (TextView) view.findViewById(R.id.print_coupon_endDate);
        ImageView couponQr = (ImageView) view.findViewById(R.id.print_coupon_qr);

        int resultFlag = 0;
        for (int i = 0, size = coupons.size(); i < size; i++) {
            Coupon coupon = coupons.get(i);
            couponNo.setText(coupon.getCouponNo());
            couponValue.setText(coupon.getCouponValue());
            couponEndDate.setText(coupon.getEndDate());
            int pix = (int) ViewUtils.dipsToPixels(300, couponQr);
            couponQr.setImageBitmap(QrUtils.createQRImage(coupon.getCouponNo(), pix, pix));
            resultFlag += printer.startPrint();
        }
        return (resultFlag == 0);
    }
}
