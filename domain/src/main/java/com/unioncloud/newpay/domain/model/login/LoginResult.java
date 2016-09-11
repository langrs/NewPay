package com.unioncloud.newpay.domain.model.login;

import com.unioncloud.newpay.domain.model.param.Code;
import com.unioncloud.newpay.domain.model.param.ExtraParam;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentMode;
import com.unioncloud.newpay.domain.model.pos.PosInfo;

import java.util.List;

/**
 * Created by cwj on 16/8/8.
 */
public class LoginResult {
    private PosInfo posInfo;
    private List<PaymentMode> paymentModeList;
    private List<Payment> paymentList;
    private List<ExtraParam> extraParamList;
    private List<Code> codes;

    public PosInfo getPosInfo() {
        return posInfo;
    }

    public void setPosInfo(PosInfo posInfo) {
        this.posInfo = posInfo;
    }

    public List<PaymentMode> getPaymentModeList() {
        return paymentModeList;
    }

    public void setPaymentModeList(List<PaymentMode> paymentModeList) {
        this.paymentModeList = paymentModeList;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public List<ExtraParam> getExtraParamList() {
        return extraParamList;
    }

    public void setExtraParamList(List<ExtraParam> extraParamList) {
        this.extraParamList = extraParamList;
    }

    public List<Code> getCodes() {
        return codes;
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
    }
}
