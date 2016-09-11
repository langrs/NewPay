package com.unioncloud.newpay.presentation.model.pos;

import com.raizlabs.coreutils.events.Event;
import com.unioncloud.newpay.domain.model.login.LoginResult;
import com.unioncloud.newpay.domain.model.param.Code;
import com.unioncloud.newpay.domain.model.param.ExtraParam;
import com.unioncloud.newpay.domain.model.payment.Payment;
import com.unioncloud.newpay.domain.model.payment.PaymentMode;
import com.unioncloud.newpay.domain.model.pos.PosInfo;
import com.unioncloud.newpay.domain.model.product.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 16/8/10.
 */
public class PosDataManager {

    private static PosDataManager instance;

    private Event<PosDataManager> dataChangedEvent = new Event<>();

    // 登录后的初始化数据
    private PosInfo posInfo;
    private List<Payment> paymentList;
    private List<PaymentMode> paymentModeList;
    private List<ExtraParam> extraParamList;
    private List<Code> codeList;
    // 查询专柜的商品数据
    private List<Product> productList = new ArrayList<>();

    public static PosDataManager getInstance() {
        if (instance == null) {
            instance = new PosDataManager();
        }
        return instance;
    }

    public Event<PosDataManager> getDataChangedEvent() {
        return dataChangedEvent;
    }

    public void onLogin(LoginResult loginResult) {
        this.posInfo = loginResult.getPosInfo();
        this.paymentList = loginResult.getPaymentList();
        this.paymentModeList = loginResult.getPaymentModeList();
        this.extraParamList = loginResult.getExtraParamList();
        this.codeList = loginResult.getCodes();
        getDataChangedEvent().raiseEvent(this);
    }

    public void onLoadProducts(List<Product> list) {
        this.productList.clear();
        this.productList.addAll(list);
        getDataChangedEvent().raiseEvent(this);
    }

    public PosInfo getPosInfo() {
        return posInfo;
    }


    public Payment getPaymentByNumberInt(int noInt) {
        for (Payment payment : paymentList) {
            if (noInt == Integer.valueOf(payment.getPaymentNumber())) {
                return payment;
            }
        }
        return null;
    }

    public Payment getPaymentById(String paymentId) {
        for (Payment payment : paymentList) {
            if (payment.getPaymentId().equals(paymentId)) {
                return payment;
            }
        }
        return null;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public List<PaymentMode> getPaymentModeList() {
        return paymentModeList;
    }

    public List<ExtraParam> getExtraParamList() {
        return extraParamList;
    }

    public List<Code> getCodeList() {
        return codeList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public Product getLocalProductById(String productId) {
        if (productId == null) {
            return null;
        }
        for (Product product : productList) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    public void clear() {
        this.posInfo = null;
        this.paymentList = null;
        this.paymentModeList = null;
        this.extraParamList = null;
        this.codeList = null;

        this.productList = null;
    }
}
