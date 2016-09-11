package com.unioncloud.newpay.presentation.ui.checkout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unioncloud.newpay.R;
import com.unioncloud.newpay.presentation.model.payment.PaymentMethod;

/**
 * Created by cwj on 16/8/4.
 */
public class PaymentMethodView extends RelativeLayout {

    private ImageView iconIamge;
    private TextView typeTv;
    private TextView numberTv;
    private TextView expirationTv;
    private TextView amountAppliedTv;

    public PaymentMethodView(Context context) {
        super(context);
        init(context);
    }

    public PaymentMethodView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PaymentMethodView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_payment_method, this);
        iconIamge = (ImageView) findViewById(R.id.list_item_payment_method_Image);
        typeTv = (TextView) findViewById(R.id.list_item_payment_method_type);
        numberTv = (TextView) findViewById(R.id.list_item_payment_method_number);
        expirationTv = (TextView) findViewById(R.id.list_item_payment_method_expiration);
        amountAppliedTv = (TextView) findViewById(R.id.list_item_payment_method_amount_applied);
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        iconIamge.setImageResource(paymentMethod.getIconRes());
        typeTv.setText(paymentMethod.getTypeName());
        numberTv.setText(paymentMethod.getNumber());
        expirationTv.setText(paymentMethod.getExpiration());
        amountAppliedTv.setText(paymentMethod.getAmountApplied());
    }

    protected void onSelected() {
        int selectedTextColor = getResources().getColor(R.color.TextColor_Dark);
        this.typeTv.setTextColor(selectedTextColor);
        this.numberTv.setTextColor(selectedTextColor);
        this.expirationTv.setTextColor(selectedTextColor);
    }

    protected void onNormal() {
        int normalTextColor = getResources().getColor(R.color.TextColor_Normal);
        this.typeTv.setTextColor(normalTextColor);
        this.numberTv.setTextColor(normalTextColor);
        this.expirationTv.setTextColor(normalTextColor);
    }

    public void setSelectedPaymentMethod(boolean isSelected) {
        if (isSelected) {
            onSelected();
        } else {
            onNormal();
        }
    }
}
