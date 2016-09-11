package com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.pay;

import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.HttpRequest;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.Signature;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.SwiftPassConst;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.SwiftPassException;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.query.QueryResult;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.query.PayQueryService;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.reverse.ReverseService;
import com.unioncloud.newpay.data.utils.XmlUtils;
import com.unioncloud.newpay.domain.model.thirdparty.TradeState;

import static java.lang.Thread.sleep;

/**
 * Created by cwj on 16/8/18.
 */
public class PayService {

    private static final int payQueryLoopCount = 3;
    private static final int WAITING_TIME = 5000;

    ReverseService reverseService;
    PayQueryService queryTrans;


    public PayService() {
        reverseService = new ReverseService();
        queryTrans = new PayQueryService();
    }

    public PayResult payBusiness(PayRequest request) throws Exception {
        String resultXml = HttpRequest.request(request);
        PayResult payResult = XmlUtils.parseFromXML(resultXml, "xml", PayResult.class);
        if (payResult == null || payResult.getStatus() == null) {
            throw new SwiftPassException("参数错误1");
        }
        if (!"0".equals(payResult.getStatus())) {
            throw new SwiftPassException("参数错误2");
        }
        if (!Signature.checkXmlSign(resultXml, SwiftPassConst.KEY)) {
            throw new SwiftPassException("验证签名失败");
        }
        if ("0".equals(payResult.getResult_code())) {   // 支付成功
            return payResult;
        }

//        return doSafeMethod(request, payResult);
        return doNewSafeMethod(request, payResult);
    }

    // 新版的安全交易事物方法.通过返回的"need_query"字段来判断是否需要轮询等待,不用再判断err_code
    private PayResult doNewSafeMethod(PayRequest request, PayResult payResult) throws Exception {
        String tradeType = payResult.getTrade_type();
        String outTradeNo = request.getOut_trade_no();
        String need_query = payResult.getNeed_query();
        String err_code = payResult.getErr_code();
        String err_message = payResult.getErr_msg();

        if ("Y".equals(need_query)) {   // 如果需要查询,就轮询等待支付结果.
            QueryResult queryResult = doNewPayQueryLoop(payQueryLoopCount, outTradeNo);
            if (queryResult != null) {
                payResult.setTrade_type(queryResult.getTrade_type());
                payResult.setOpenid(queryResult.getOpenid());
                payResult.setTransaction_id(queryResult.getTransaction_id());
                payResult.setSub_appid(queryResult.getSub_appid());
                payResult.setOut_trade_no(queryResult.getOut_trade_no());
                payResult.setOut_transaction_id(queryResult.getOut_transaction_id());
                payResult.setTotal_fee(queryResult.getTotal_fee());
                payResult.setCoupon_fee(queryResult.getCoupon_fee());
                payResult.setBank_type(queryResult.getBank_type());
                payResult.setBank_billno(queryResult.getBank_billno());
                payResult.setFee_type(queryResult.getFee_type());
                payResult.setTime_end(queryResult.getTime_end());
                payResult.setAttach(queryResult.getAttach());
                return payResult;
            }
        }
        doReverseLoop(outTradeNo);
        throw new SwiftPassException("支付失败:"+err_message);
    }

    // 执行安全的返回支付结果
    private PayResult doSafeMethod(PayRequest request, PayResult payResult) throws Exception {
        String tradeType = payResult.getTrade_type();
        String outTradeNo = request.getOut_trade_no();
        String err_code = payResult.getErr_code();
        String err_message = payResult.getErr_msg();

        if (SwiftPassConst.TradeType.WEIXIN.getKey().equals(tradeType)) {
            if ("AUTHCODEEXPIRE".equals(err_code)               // 授权码过期
                    || "AUTH_CODE_INVALID".equals(err_code)     // 授权码无效
                    || "NOTENOUGH".equals(err_code)) {          // 余额不足
                doReverseLoop(outTradeNo);
                throw new SwiftPassException(err_message);
            }
            if ("USERPAYING".equals(err_code)) {         // 等待用户输入密码
                if (doPayQueryLoop(payQueryLoopCount, outTradeNo)) {
                    return payResult;
                } else {
                    doReverseLoop(outTradeNo);
                    throw new SwiftPassException("支付超时");
                }
            }
            if (doPayQueryLoop(payQueryLoopCount, outTradeNo)) {
                return payResult;
            } else {
                doReverseLoop(outTradeNo);
                throw new SwiftPassException(err_message);
            }
        } else if (SwiftPassConst.TradeType.QQ.getKey().equals(tradeType)) {
            if ("66227005".equals(err_code)) {  // 等待用户输入密码
                if (doPayQueryLoop(payQueryLoopCount, outTradeNo)) {
                    return payResult;
                } else {
                    doReverseLoop(outTradeNo);
                    throw new SwiftPassException("支付超时");
                }
            }
            if ("66227006".equals(err_code)             // 授权码(二维码)无效
                    || "66227008".equals(err_code)) {   // 或者余额不足
                doReverseLoop(outTradeNo);
                throw new SwiftPassException(err_message);
            }
            if (doPayQueryLoop(payQueryLoopCount, outTradeNo)) {
                return payResult;
            } else {
                doReverseLoop(outTradeNo);
                throw new SwiftPassException(err_message);
            }
        } else if (SwiftPassConst.TradeType.ALIPAY.getKey().equals(tradeType)) {
            if (doPayQueryLoop(payQueryLoopCount, outTradeNo)) {
                return payResult;
            } else {
                doReverseLoop(outTradeNo);
                throw new SwiftPassException(err_message);
            }
        }
        throw new SwiftPassException("未知的支付方式");
    }

    private void doReverseLoop(String outTradeNo) throws Exception{
        reverseService.doReverseLoop(outTradeNo);
    }

    private QueryResult doNewPayQueryLoop(int loopCount, String outTradeNo) throws Exception{
        //至少查询一次
        if (loopCount == 0) {
            loopCount = 1;
        }
        //进行循环查询
        for (int i = 0; i < loopCount; i++) {
            // waiting for update trade state
            sleep(WAITING_TIME);//等待一定时间再进行查询，避免状态还没来得及被更新

            QueryResult queryResult = queryTrans.query(outTradeNo, null);
            if (queryResult == null || queryResult.getStatus() == null) {
                // 支付订单查询请求逻辑错误，请仔细检测传过去的每一个参数是否合法
                continue;
            }
            if (!"0".equals(queryResult.getStatus())) {
                //注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
                continue;
            }
            if (TradeState.SUCCESS.getTag().equals(queryResult.getTrade_state())) {
                return queryResult;
            }
        }
        return null;
    }

    private boolean doPayQueryLoop(int loopCount, String outTradeNo) throws Exception{
        //至少查询一次
        if (loopCount == 0) {
            loopCount = 1;
        }
        //进行循环查询
        for (int i = 0; i < loopCount; i++) {
            if (doOnePayQuery(outTradeNo)) {
                return true;
            }
        }
        return false;
    }

    private boolean doOnePayQuery(String outTradeNo) throws Exception {
        // waiting for update trade state
        sleep(WAITING_TIME);//等待一定时间再进行查询，避免状态还没来得及被更新

        QueryResult queryResult = queryTrans.query(outTradeNo, null);
        if (queryResult == null || queryResult.getStatus() == null) {
            // 支付订单查询请求逻辑错误，请仔细检测传过去的每一个参数是否合法
            return false;
        }
        if (!"0".equals(queryResult.getStatus())) {
            //注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
            return false;
        }
        if (TradeState.SUCCESS.getTag().equals(queryResult.getTrade_state())) {
            return true;
        }
        return false;
    }
}
