package com.unioncloud.newpay.presentation.ui.register;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.unioncloud.newpay.R;

/**
 * Created by cwj on 16/8/12.
 */
public class RegisterViewBinder {
//    public EditText shopNoEt;
    public EditText storeNoEt;
    public EditText userNoEt;
    public EditText posNoEt;
    public Spinner posTypeSpinner;
    public Button registerBtn;


    public int getLayout() {
        return R.layout.fragment_register;
    }

    public void bindView(View view) {
//        shopNoEt = (EditText) view.findViewById(R.id.register_shop);
        storeNoEt = (EditText) view.findViewById(R.id.register_store);
        userNoEt = (EditText) view.findViewById(R.id.register_user);
        posNoEt = (EditText) view.findViewById(R.id.register_pos_number);
        posTypeSpinner = (Spinner) view.findViewById(R.id.register_pos_type);
        registerBtn = (Button) view.findViewById(R.id.register_register);
    }
}
