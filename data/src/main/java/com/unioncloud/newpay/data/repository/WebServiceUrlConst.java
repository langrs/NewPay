package com.unioncloud.newpay.data.repository;

import com.unioncloud.newpay.data.BuildConfig;

/**
 * Created by cwj on 16/8/12.
 */
public class WebServiceUrlConst {

    private static final String HOST_RELEASE = "http://183.239.195.196:8288";
    private static final String HOST_DEBUG = "http://1565s67w79.iok.la:14371";

    public static final  String NAMESPACE = "http://webService.site.com/";

    /** 注册POS机 */
    public static final String POS_REGISTER_ACTION = "posRegister";
    /** 登录 */
    public static final String LOGIN_ACTION = "userVerify";
    /** 获取商品信息 */
    public static final String QUERY_PRODUCT_ACTION = "queryItem";
    /** 结算(订单),返回订单折扣/优惠后的金额 */
    public static final String CHECKOUT_ACTION = "preSale";
    /** 检测是否能够使用特定的支付方式 */
    public static final String CHECK_PAYMENT = "prepay";
    /** 提交销售信息 */
    public static final String SUBMIT_SALE = "sale";
    /** 查询销售信息 */
    public static final String QUERY_SALE = "saleQuery";

    /** 查询VIP信息 */
    public static final String QUERY_VIP = "vipQuery";
    /** 查询储值卡 */
    public static final String QUERY_GIFT_CARD = "czkQuery";
    /** (现金)券查询 */
    public static final String QUERY_COUPON = "couponQuery";
    /** 修改密码 */
    public static final String CHANGE_PASSWORD = "changePasswd";

    /** 查询通知 */
    public static final String QUERY_NOTICE = "queryNotice";

    /** 查询(会员的)积分返利信息 */
    public static final String QUERY_POINTS_REBATE = "queryJffl";
    /** 生成积分返利券 */
    public static final String CREATE_POINTS_REBATE_COUPON = "makeJffl";
    /** 生成销售返券 */
    public static final String CREATE_SALE_COUPON = "makeCoupon";
    /** 查询退货权限 */
    public static final String QUERY_REFUND_RIGHT = "getRtnRight";

    public static String getHost() {
        if (BuildConfig.DEBUG) {
            return HOST_DEBUG;
        }
        return HOST_RELEASE;
    }

    public static String getUrl() {
        String host;
        if (!BuildConfig.DEBUG) {
            host = HOST_RELEASE;
        } else {
            host = HOST_DEBUG;
        }
        return host + "/mpos_web/service/saleWebService?wsdl";
    }

    /** 应用升级 */
    public static String getAppUpgrade() {
        String host;
        if (!BuildConfig.DEBUG) {
            host = HOST_RELEASE;
        } else {
            host = HOST_DEBUG;
        }
        return host + "/update/appInfo.txt";
    }
}
