package com.unioncloud.newpay.data.repository.coupon.datasource.wechat;

public class WeChatCouponConst {
//    汕头合胜的公总号信息
    public static final String APP_ID = "wx8ccb7b33fed797a8";
    public static final String SECRET = "5c860297feca4f9d6d96bf1771d5cdab";
//坦洲
//    public static final String APP_ID = "wx1603d709d0417fc1";
//    public static final String SECRET = "ff339a5efe268a7aa775104c2be17ae6";

    //获取access_token
    public static final String URL_GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";
//    查询微信卡券
    public static final String URL_QUERY_CODE = "https://api.weixin.qq.com/card/code/get";
//    通过cardId查询card资料
    public static final String URL_QUERY_WECHAR_CARD = "https://api.weixin.qq.com/card/get";
    //    核销微信卡券
    public static final String URL_CONSUME_CODE = "https://api.weixin.qq.com/card/code/consume";
//连接超时时长
    public static final int TIMEOUT = 30 * 1000;

}