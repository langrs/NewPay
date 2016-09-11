package com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.refund;

import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.Signature;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.SwiftPassConst;
import com.unioncloud.newpay.domain.utils.RandomStringGenerator;

/**
 * Created by cwj on 16/7/21.
 */
public class RefundQueryRequest {
    String service;
    String version;
    String charset;
    String sign_type;
    String mch_id;
    String out_trade_no;
    String transaction_id;
    String out_refund_no;
    String refund_id;
    String nonce_str;
    String sign;

    public RefundQueryRequest(String orderId, String refundOrderId,
                              String transactionId, String refundId) {
        setService(SwiftPassConst.REFUND_QUERY_SERVICE);
        setMch_id(SwiftPassConst.MCH_ID);
        setTransaction_id(transactionId);
        setRefund_id(refundId);
        setOut_trade_no(orderId);
        setOut_refund_no(refundOrderId);
        setMch_id(SwiftPassConst.MCH_ID);
        setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
        setSign(Signature.getSign(this, SwiftPassConst.KEY));
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public String getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
