package com.unioncloud.pax;

/**
 * 定义了百富API的公共参数中transType可以参数的字符串.
 * 以TRADE_开头的字符常量表示交易操作; 以OP_开头的字符串表示管理操作.
 */
public class PaxConstants {

    // 交易请求中的用来表明自身APP标记. 默认使用该值, 可以通过PaxRequest.setXXX变更
    public static final String APP_ID = "com.unioncloud.Payment";
    public static final String APP_NAME = "智云支付";

    /**
     * 消费
     */
    public static final String TRADE_SALE = "SALE";

    /**
     * 撤销
     */
    public static final String TRADE_VOID = "VOID";

    /**
     * 退货
     */
    public static final String TRADE_REFUND = "REFUND";

    /**
     * 预授权
     */
    public static final String TRADE_AUTH = "AUTH";

    /**
     * 预授权撤销
     */
    public static final String TRADE_AUTH_VOID = "AUTH_VOID";

    /**
     * 预授权完成请求
     */
    public static final String TRADE_AUTH_CM = "AUTH_CM";

    /**
     * 预授权完成请求撤销
     */
    public static final String TRADE_AUTH_CM_VOID = "AUTH_CM_VOID";

    /**
     * 预授权完成通知
     */
    public static final String TRADE_AUTH_ADV = "AUTH_ADV";

    /**
     * 余额查询
     */
    public static final String TRADE_BALANCE = "BALANCE";

    /**
     * 扫码消费
     */
    public static final String TRADE_QR_SALE = "QR_SALE";

    /**
     * 扫码撤销
     */
    public static final String TRADE_QR_VOID = "QR_VOID";

    /**
     * 扫码退货
     */
    public static final String TRADE_QR_REFUND = "QR_REFUND";

    /**
     * 签到
     */
    public static final String OP_LOGON = "LOGON";

    /**
     * 签退
     */
    public static final String OP_LOGOFF = "LOGOFF";

    /**
     * 结算
     */
    public static final String OP_SETTLE = "SETTLE";

    /**
     * 打印最后一笔
     */
    public static final String OP_PRINT_LAST = "PRN_LAST";

    /**
     * 重打印任意笔
     */
    public static final String OP_PRINT_ANY = "PRN_ANY";

    /**
     * 打印交易明细
     */
    public static final String OP_PRINT_DETAIL = "PRN_DETAIL";

    /**
     * 打印交易汇总
     */
    public static final String OP_PRINT_TOTAL = "PRN_TOTAL";

    /**
     * 重打结算单
     */
    public static final String OP_PRINT_LAST_BATCH = "PRN_LAST_BATCH";

    /**
     * 获取卡号
     */
    public static final String OP_GET_CARD_NO = "GET_CARD_NO";

    /**
     * 终端参数设置
     */
    public static final String OP_SETTING = "SETTING";

    /**
     * 打印图片
     */
    public static final String OP_PRINT_BITMAP = "PRN_BITMAP";

    /**
     * 交易查询
     */
    public static final String OP_TRANSACTION_QUERY = "TRANS_QUERY";

}
