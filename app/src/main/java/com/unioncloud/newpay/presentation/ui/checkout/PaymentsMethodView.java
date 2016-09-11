package com.unioncloud.newpay.presentation.ui.checkout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.unioncloud.newpay.R;

/**
 * Created by cwj on 16/8/4.
 */
public class PaymentsMethodView extends LinearLayout {

    private ViewFlipper methodFlipper;
    private Button addMethodBtn;
    private PaymentMethodView defaultMethodView;
//    private Button d;
//    private LinearLayout e;
//    private Button f;
    private int addBtnIndex;
    private int defaultMethodViewIndex;
    private AddPaymentMethodListener addListener;

    public PaymentsMethodView(Context context) {
        super(context);
        init(context);
    }

    public PaymentsMethodView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PaymentsMethodView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_payments_method_view, this);
        methodFlipper = (ViewFlipper) findViewById(R.id.view_payments_method_view_flipper);
        addMethodBtn = (Button) findViewById(R.id.view_payments_method_view_add_method_button);
        defaultMethodView = (PaymentMethodView) findViewById(R.id.view_payments_method_view_default_method_view);
        addBtnIndex = methodFlipper.indexOfChild(addMethodBtn);
        defaultMethodViewIndex = methodFlipper.indexOfChild(defaultMethodView);
        addMethodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addListener != null) {
                    addListener.onAddPaymentMethod();
                }
            }
        });
    }


}
