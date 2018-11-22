package com.unioncloud.newpay.data.repository.coupon.datasource.wechat;

import com.google.gson.Gson;

import org.xutils.http.RequestParams;
import org.xutils.x;

public class WeChatCouponService {
//    获取access_token,失败返回null
    public String getAccessToken(){
        String url = WeChatCouponConst.URL_GET_ACCESS_TOKEN + "?grant_type=client_credential&appid=" + WeChatCouponConst.APP_ID + "&secret=" + WeChatCouponConst.SECRET;
        RequestParams params = new RequestParams(url);
        params.setCharset("UTF-8");
        try{
            String result = x.http().postSync(params,String.class);
            Gson gson = new Gson();
            WeChatCouponResponse weChatCouponResponse = gson.fromJson(result, WeChatCouponResponse.class);
            if (weChatCouponResponse.getErrcode() == 0){
                return weChatCouponResponse.getAccess_token();
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return null;
//        return "15_QYjSP3YOcX5PpygC1Ple-TDiII1c606MfymaQh9aPMyboI2DZS5YK91wBcuv8x3MkxzF9JY0IwCo6bQgACJnzjGL3i86wM0sWuY-F_UjLPcZQ-gwlSlB3ifpX0Ldo0qRjuK6uChJnCmSkpwnSZZaAFAOVV";
    }

//    查找code是否可用
    public WeChatCouponResponse getWeChatCouponCode(String accessToken,String code){
        WeChatCouponResponse rtn = new WeChatCouponResponse();
        rtn.setErrcode(100);
        rtn.setErrmsg("");
        String url = WeChatCouponConst.URL_QUERY_CODE;
        String require = "{\"code\" : \""+code +"\",\"check_consume\" : true}";
        RequestParams params = new RequestParams(url+"?access_token=" + accessToken);
        params.setConnectTimeout(WeChatCouponConst.TIMEOUT);
        params.setMaxRetryCount(0);
        params.setCharset("UTF-8");
        params.setBodyContent(require);
        try {
            String result = x.http().postSync(params,String.class);
            Gson gson = new Gson();
            rtn = gson.fromJson(result,WeChatCouponResponse.class);
        }catch (Throwable throwable){
            throwable.printStackTrace();
            rtn.setErrmsg("查询微信卡券信息出错");
        }
        return rtn;
    }
    //通过card_id查询卡面值,卡券必须是通过[代金券]这种方式来制作的才可以
    public WeChatCard getCardInfo(String accessToken,String code){
        WeChatCard rtn = new WeChatCard();
        rtn.setErrcode("100");
        String url = WeChatCouponConst.URL_QUERY_WECHAR_CARD;
        String require = "{\"card_id\" : \""+code +"\"}";
        RequestParams params = new RequestParams(url+"?access_token=" + accessToken);
        params.setConnectTimeout(WeChatCouponConst.TIMEOUT);
        params.setMaxRetryCount(0);
        params.setCharset("UTF-8");
        params.setBodyContent(require);
        try {
            String result = x.http().postSync(params,String.class);
            Gson gson = new Gson();
            rtn = gson.fromJson(result,WeChatCard.class);
        }catch (Throwable throwable){
            throwable.printStackTrace();
            rtn.setErrmsg("查询微信卡券详细信息出错");
        }
        return rtn;
    }
//    核销微信卡券
    public WeChatCouponResponse consumeWeChatCouponCode(String code,String accessToken){
        WeChatCouponResponse rtn = new WeChatCouponResponse();
        rtn.setErrcode(100);
        rtn.setErrmsg("");
        String url = WeChatCouponConst.URL_CONSUME_CODE;
        String require = "{\"code\" : \""+code +"\"}";
        RequestParams params = new RequestParams(url+"?access_token=" + accessToken);
        params.setConnectTimeout(WeChatCouponConst.TIMEOUT);
        params.setMaxRetryCount(0);
        params.setCharset("UTF-8");
        params.setBodyContent(require);
        try {
            String result = x.http().postSync(params,String.class);
            Gson gson = new Gson();
            WeChatCouponResponse weChatCouponResponse = gson.fromJson(result,WeChatCouponResponse.class);
            if(weChatCouponResponse.getErrcode()==0){
                rtn.setErrcode(0);
            }else{
                rtn.setErrmsg(weChatCouponResponse.getErrmsg());
            }
        }catch (Throwable throwable){
            rtn.setErrmsg("核销失败");
        }
        return rtn;
    }
    public WeChatCouponResponse dealWeChatCouponCode(String code,String accessToken){
        WeChatCouponResponse rtn = new WeChatCouponResponse();
        rtn.setErrcode(100);
        rtn.setErrmsg("");
//        1.检查code是否为空
        if(code == null || code.length() ==0){
            rtn.setErrmsg("核销码不能为空");
            return rtn;
        }

//        2.获取accessToken
//        String accessToken = getAccessToken();
//        if(accessToken == null){
//            rtn.setErrmsg("获取ACCESS_TOKEN失败");
//            return rtn;
//        }
//        3.查询code的信息
        WeChatCouponResponse weChatCouponResponse = getWeChatCouponCode(accessToken,code);
        if(weChatCouponResponse.getErrcode() != 0){
            rtn.setErrmsg(weChatCouponResponse.getErrmsg());
            return rtn;
        }
        if(!weChatCouponResponse.isCan_consume()){
            rtn.setErrmsg("卡券已经失效");
            return rtn;
        }
//        3.3坦洲修改获取面值
        String cardId = weChatCouponResponse.getCard().getCard_id();
//        卡面值
        Integer reduceCost = 0;
        WeChatCard weChatCard = getCardInfo(accessToken,cardId);
        if(weChatCard != null){
            if(weChatCard.getErrcode().equals("0")){
                if(!weChatCard.getCard().getCard_type().equals("CASH")){
                    rtn.setErrmsg("卡类型有误");
                    return rtn;
                }else{
//                    卡面值
                    reduceCost = weChatCard.getCard().getCash().getReduce_cost();
                }
            }else{
                rtn.setErrmsg(weChatCard.getErrmsg());
                return rtn;
            }
        }else{
            rtn.setErrmsg("查询微信卡券详细信息出错!");
            return rtn;
        }
//        4.核销code
        weChatCouponResponse = consumeWeChatCouponCode(code,accessToken);
        if(weChatCouponResponse.getErrcode()!= 0 ){
            rtn.setErrmsg(weChatCouponResponse.getErrmsg());

        }else{
            rtn.setErrcode(0);
            //            赋值卡面值
            rtn.setReduce_cost(reduceCost);
        }
        return rtn;
    }
}