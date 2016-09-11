package com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.reverse;

import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.Signature;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.SwiftPassConst;
import com.unioncloud.newpay.domain.utils.RandomStringGenerator;

/**
 * Created by cwj on 16/7/21.
 */
public class ReverseRequest {
    String service;
    String mch_id;          // 商户号
    String out_trade_no;    // 商户自身的订单号
    String nonce_str;       // 随机字符串
    String sign;            // MD5签名结果

    // 选填项
    String version;         // 版本号(默认"2.0")
    String charset;         // 字符集(默认"UTF-8")
    String sign_type;       // 签名方式(默认"MD5")

    public ReverseRequest(String outTradeNo) {
        setService(SwiftPassConst.REVERSE_SERVICE);
        setMch_id(SwiftPassConst.MCH_ID);
        setOut_trade_no(outTradeNo);
        setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
        setSign(Signature.getSign(this, SwiftPassConst.KEY));
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
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
}
