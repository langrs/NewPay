package com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.refund;

import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.Signature;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.SwiftPassConst;
import com.unioncloud.newpay.domain.utils.RandomStringGenerator;

/**
 * Created by cwj on 16/7/21.
 */
public class RefundRequest {
    String service;
    String mch_id;
    String out_trade_no;
    String transaction_id;
    String out_refund_no;
    int total_fee;
    int refund_fee;
    String op_user_id;
    String nonce_str;
    String sign;

    // 选填项
    String version;
    String charset;
    String sign_type;
    String refund_channel;      // 退款渠道

    public RefundRequest(String outTradeNo, String outRefundNo,
                         String transactionId,
                         int totalFee, int refundFee) {
        setService(SwiftPassConst.REFUND_SERVICE);
        setMch_id(SwiftPassConst.MCH_ID);
        setOut_trade_no(outTradeNo);
        setOut_refund_no(outRefundNo);
        setTransaction_id(transactionId);
        setTotal_fee(totalFee);
        setRefund_fee(refundFee);
        setOp_user_id(SwiftPassConst.MCH_ID);
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

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public int getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(int refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getOp_user_id() {
        return op_user_id;
    }

    public void setOp_user_id(String op_user_id) {
        this.op_user_id = op_user_id;
    }

    public String getRefund_channel() {
        return refund_channel;
    }

    public void setRefund_channel(String refund_channel) {
        this.refund_channel = refund_channel;
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
