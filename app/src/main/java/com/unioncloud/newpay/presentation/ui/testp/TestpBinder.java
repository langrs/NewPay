package com.unioncloud.newpay.presentation.ui.testp;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unioncloud.newpay.R;

public class TestpBinder {
    public Button btn_ok;
    public Button btn_cancel;
    public EditText et_ckcode_input;
    public EditText et_type_input;
    public TextView tv_ckcode;
    public TextView tv_custAddr;
    public TextView tv_custName;
    public TextView tv_mkcode;
    public TextView tv_custMobile;

    public void onBind(View view){
        btn_ok = (Button) view.findViewById(R.id.testp_ok);
        btn_cancel = (Button) view.findViewById(R.id.testp_cancel);
        et_ckcode_input = (EditText) view.findViewById(R.id.testp_ckcode_input);
        et_type_input = (EditText) view.findViewById(R.id.testp_input_type);
        tv_ckcode = (TextView) view.findViewById(R.id.testp_ckcode);
        tv_custAddr = (TextView) view.findViewById(R.id.testp_custAddr);
        tv_custName = (TextView) view.findViewById(R.id.testp_custName);
        tv_mkcode = (TextView) view.findViewById(R.id.testp_mkcode);
        tv_custMobile = (TextView) view.findViewById(R.id.testp_custMobile);
    }

}