package com.unioncloud.newpay.data.repository;

/**
 * Created by cwj on 16/8/12.
 */
public class WebServiceUrlConst {

    public static final String URL = "http://183.239.195.196:8288/mpos_web/service/saleWebService?wsdl";
    public static final String NAMESPACE = "http://webService.site.com/";

    /** 应用升级 */
    public static final String APPUPGRADE = "http://183.239.195.196:8288/update/appInfo.txt";

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
}
