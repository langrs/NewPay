package com.unioncloud.newpay.presentation.ui.placeorder;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.raizlabs.coreutils.view.ViewUtils;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.product.Product;
import com.unioncloud.newpay.domain.utils.MoneyUtils;


/**
 * Created by cwj on 16/8/9.
 */
public class ProductChooseViewBinder implements AdapterView.OnItemClickListener {
//    public Spinner productNoSpinner;
    public TextView productNoSpinner;
    public TextView productNameTv;
    public EditText priceEt;
    public TextView priceFixedTv;   // 定价
    public Spinner quantitySpinner;
    public TextView addToCartBtn;
    public TextView checkoutBtn;
    private Context context;
    ProductAdapter adapter;
    ListPopupWindow popupWindow;
    private int selectedProductIndex;

    public void bindView(View view) {
        context = view.getContext();
        productNoSpinner = (TextView) view.findViewById(R.id.layout_place_order_product_number_spinner);
        productNoSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopProducts();
            }
        });
        productNameTv = (TextView) view.findViewById(R.id.layout_place_order_product_name);
        priceEt = (EditText) view.findViewById(R.id.layout_place_order_product_price);
        priceFixedTv = (TextView) view.findViewById(R.id.layout_place_order_product_price_fixed);
        quantitySpinner = (Spinner) view.findViewById(R.id.layout_place_order_product_quantity);
        addToCartBtn = (TextView) view.findViewById(R.id.layout_place_order_product_add_button);
        checkoutBtn = (TextView) view.findViewById(R.id.layout_place_order_checkout_button);
    }

    public void selectedProduct(Product product) {
        if (product != null) {
            productNoSpinner.setText(product.getProductNumber());
            productNameTv.setText(product.getProductName());
            if (product.isFixPrice()) {
                priceFixedTv.setText(String.valueOf(product.getPrice()));
                priceFixedTv.setVisibility(View.VISIBLE);
                priceEt.setText(null);
                priceEt.setVisibility(View.GONE);
            } else {
                priceFixedTv.setText(String.valueOf(0));
                priceFixedTv.setVisibility(View.GONE);
                priceEt.setText(null);
                priceEt.setVisibility(View.VISIBLE);
            }
        } else {
            productNameTv.setText(null);
            priceEt.setText(null);
            priceEt.setVisibility(View.VISIBLE);
            priceFixedTv.setText(String.valueOf(0));
            priceFixedTv.setVisibility(View.GONE);

            quantitySpinner.setSelection(0);
            productNoSpinner.setText(R.string.select_product_prompt);
        }
    }

    public void setAdapter(ProductAdapter adapter) {
        this.adapter = adapter;
    }

    public int getCustomPrice() {
        if (priceEt.getVisibility() == View.VISIBLE) {
            String priceStr = priceEt.getText().toString();
            return MoneyUtils.getFen(priceStr);
        }
        return 0;
    }

    public void resetView() {
        selectedProduct(null);
        selectedProductIndex = -1;
    }

    public void showPopProducts() {
        if (popupWindow == null) {
            popupWindow = new ListPopupWindow(context);
            popupWindow.setAdapter(adapter);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setContentWidth(productNoSpinner.getWidth() - productNoSpinner.getPaddingRight());
            popupWindow.setAnchorView(productNoSpinner);
            popupWindow.setVerticalOffset((int) ViewUtils.dipsToPixels(-40, productNoSpinner));
            popupWindow.setOnItemClickListener(this);
        }
        popupWindow.show();

    }

    public Product getSelectedItem() {
        if (selectedProductIndex < 0 || selectedProductIndex >= adapter.getCount()) {
            return null;
        }
        return adapter.getItem(selectedProductIndex);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedProductIndex = position;
        selectedProduct(adapter.getItem(position));
        popupWindow.dismiss();
    }
}
