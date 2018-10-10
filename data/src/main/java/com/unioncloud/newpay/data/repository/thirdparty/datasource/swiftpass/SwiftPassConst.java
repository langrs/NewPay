package com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass;

/**
 * Created by cwj on 16/7/21.
 */
public class SwiftPassConst {

    public static final String URL = "https://pay.swiftpass.cn/pay/gateway";

    public static final int TIMEOUT = 30 * 1000;

    /** 支付服务名 */
    public static final String PAY_SERVICE = "unified.trade.micropay";
    /** 查询订单服务名 */
    public static final String QUERY_SERVICE = "unified.trade.query";
    /** 冲正服务名 */
    public static final String REVERSE_SERVICE = "unified.micropay.reverse";
    /** 退款服务名 */
    public static final String REFUND_SERVICE = "unified.trade.refund";
    /** 退款状态查询服务名 */
    public static final String REFUND_QUERY_SERVICE = "unified.trade.refundquery";

//////////////////////////////    汕头商户id和key  ///////////////////////////////////////
//   public static final String MCH_ID = "133500001422";
//    public static final String KEY = "01269e5f1a77eba91c97daa768633b5d";
///////////////////////////////////////////////////////////////////////////////


////////////////////////////    坦洲商户id和key  /////////////////////////////////////////
    public static final String MCH_ID = "101540254692";
    public static final String KEY = "5976fe4be751c69b067f9b6372e84056";
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////测试的MD5签名的KEY/////////////////////////////////
    //    public static final String MCH_ID = "7541100001";
//public static final String KEY = "3fb765b25fd9e7191993c245740042b9";


//public static final String MCH_ID_DEBUD = "7551000001";
//    public static final String KEY_DEBUD = "9d101c97133837e13dde2d32a5054abb";
//    ///////////////////////////////////////////////////////////////////////////////


    public static final int LOOP_WAITING_TIME = 5000;

    public enum TradeType {
        WEIXIN("pay.weixin.micropay", "微信支付"),
        QQ("pay.qq.micropay", "QQ支付"),
        ALIPAY("pay.alipay.micropay", "支付宝支付"),
        JDPAY("pay.jdpay.micropay", "京东支付");

        String key;
        String name;

        TradeType(String key, String name) {
            this.key = key;
            this.name = name;
        }

        public static String findNameByKey(String key) {
            if (WEIXIN.key.equals(key)) {
                return WEIXIN.name;
            } else if (ALIPAY.key.equals(key)) {
                return ALIPAY.name;
            } else if (JDPAY.key.equals(key)) {
                return JDPAY.name;
            } else if (QQ.key.equals(key)) {
                return QQ.name;
            }
            return null;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
