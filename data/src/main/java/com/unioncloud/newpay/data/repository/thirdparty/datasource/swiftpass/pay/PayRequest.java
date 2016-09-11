package com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.pay;

import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.Signature;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.SwiftPassConst;
import com.unioncloud.newpay.domain.utils.RandomStringGenerator;

/**
 * Created by cwj on 16/7/21.
 */
public class PayRequest {
    String service;
    String mch_id;          // 商户号
    String groupno;         // 大商户号(如果不为空,签名使用大商户密钥)
    String out_trade_no;    // 商户自身的订单号
    String body;            // 商品描述
    int total_fee;          // 总金额(单位是分)
    String mch_create_ip;   // 订单生成的机器IP
    String auth_code;       // 授权码,扫描的二维码
    String nonce_str;       // 随机字符串
    String sign;            // MD5签名结果

    // 选填项
    String version;         // 版本号(默认"2.0")
    String charset;         // 字符集(默认"UTF-8")
    String sign_type;       // 签名方式(默认"MD5")
    String device_info;     // 设备号
    String attach;          // 附加信息
    String time_start;      // 订单生成时间(yyyymmddhhmmss)
    String time_expire;     // 订单失效时间(yyyymmddhhmmss)
    String op_user_id;      // 操作人员ID
    String op_shop_id;      // 门店编号
    String op_device_id;    // 设备编号
    String goods_tag;       // 商品标记

    public PayRequest(String outTradeNo, String body, String attach, String barcode,
                      int totalFee, String timeStart, String timeExpire, String ip) {
        setService(SwiftPassConst.PAY_SERVICE);
        setMch_id(SwiftPassConst.MCH_ID);
        setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
        setOut_trade_no(outTradeNo);
        setBody(body);
        setAttach(attach);
        setAuth_code(barcode);
        setTotal_fee(totalFee);
        setTime_start(timeStart);
        setTime_expire(timeExpire);
        setMch_create_ip(ip);
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

    public String getGroupno() {
        return groupno;
    }

    public void setGroupno(String groupno) {
        this.groupno = groupno;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getMch_create_ip() {
        return mch_create_ip;
    }

    public void setMch_create_ip(String mch_create_ip) {
        this.mch_create_ip = mch_create_ip;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
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

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_expire() {
        return time_expire;
    }

    public void setTime_expire(String time_expire) {
        this.time_expire = time_expire;
    }

    public String getOp_user_id() {
        return op_user_id;
    }

    public void setOp_user_id(String op_user_id) {
        this.op_user_id = op_user_id;
    }

    public String getOp_shop_id() {
        return op_shop_id;
    }

    public void setOp_shop_id(String op_shop_id) {
        this.op_shop_id = op_shop_id;
    }

    public String getOp_device_id() {
        return op_device_id;
    }

    public void setOp_device_id(String op_device_id) {
        this.op_device_id = op_device_id;
    }

    public String getGoods_tag() {
        return goods_tag;
    }

    public void setGoods_tag(String goods_tag) {
        this.goods_tag = goods_tag;
    }
}
