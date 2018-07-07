package com.unioncloud.newpay.presentation.ui.cart.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.raizlabs.coreutils.view.ViewUtils;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.cart.CartItem;
import com.unioncloud.newpay.domain.utils.MoneyUtils;
import com.unioncloud.newpay.presentation.ui.cart.CartItemMenuListener;

/**
 * Created by cwj on 16/8/13.
 */
public class CartItemView extends LinearLayout {

    private LinearLayout descriptionLayout; // 描述商品属性的Layout

    private TextView productNameTv;
    private TextView productNoTv;
    private View priceContainerView;    // 价格展示区域(包含正价和打折)
    private TextView priceTv;
    private TextView promTv;        // 促销信息
    private Spinner quantitySpinner; // 数量
    private View rootView;

    private CartItemMenuListener itemMenuListener;

    public CartItemView(Context context) {
        super(context);
        init(context);
    }

    public CartItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CartItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.view_cart_object, this);
        descriptionLayout = (LinearLayout) findViewById(R.id.view_cart_object_desc_layout);
        productNameTv = (TextView) findViewById(R.id.view_cart_object_name);
        productNoTv = (TextView) findViewById(R.id.view_cart_object_number);
        priceContainerView = findViewById(R.id.view_cart_object_price_container);
        priceTv = (TextView) findViewById(R.id.view_cart_object_price);
        promTv = (TextView) findViewById(R.id.view_cart_object_prom);
        quantitySpinner = (Spinner) findViewById(R.id.view_cart_object_quantity);

        rootView = findViewById(R.id.view_cart_object_background_item_container);

        priceContainerView.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if (itemMenuListener != null) {
                    itemMenuListener.onItemMenu(v);
                }
            }
        });
    }

    public void setCartItem(final CartItem cartItem) {
        if (cartItem != null) {
            priceTv.setText(String.format("¥ %1s", MoneyUtils.fenToString(cartItem.getSellUnitPrice())));
            String discountInfo = cartItem.getPromInfo();
            promTv.setText(discountInfo);

            productNameTv.setText(cartItem.getProductName());
            productNoTv.setText(cartItem.getProductNumber());

            quantitySpinner.setAdapter(new QuantityAdapter());
            quantitySpinner.setSelection(Math.abs(cartItem.getQuantity()) - 1);
            quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    int newQuantity = position + 1;
                    if (Math.abs(cartItem.getQuantity()) != newQuantity
                            && itemMenuListener != null) {
                        itemMenuListener.onItemQuantityChanged(cartItem, newQuantity);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            setProductViewTag(cartItem);
        }

    }

    public void setCartItemMenuListener(CartItemMenuListener listener) {
        this.itemMenuListener = listener;
    }

    public void setProductOnClickListener(OnClickListener listener) {
        this.rootView.setOnClickListener(listener);
    }

    private void setProductViewTag(CartItem cartItem) {
        this.priceContainerView.setTag(cartItem);
        this.rootView.setTag(cartItem);
    }
}
