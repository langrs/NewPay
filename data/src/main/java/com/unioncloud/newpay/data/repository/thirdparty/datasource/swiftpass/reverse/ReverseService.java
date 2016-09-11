package com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.reverse;

import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.HttpRequest;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.SwiftPassConst;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.SwiftPassException;
import com.unioncloud.newpay.data.utils.XmlUtils;

import static java.lang.Thread.sleep;

/**
 * Created by cwj on 16/7/22.
 */
public class ReverseService {
    //是否需要再调一次撤销，这个值由撤销API回包的result_code字段决定
    private boolean needRecallReverse = true;

    public void doReverseLoop(String outTradeNo) throws Exception {
        //初始化这个标记
        needRecallReverse = true;
        //进行循环撤销，直到撤销成功，或是API返回recall字段为"Y"
        while (needRecallReverse) {
            if (doOneReverse(outTradeNo)) {
                return;
            }
        }
    }

    private boolean doOneReverse(String outTradeNo) throws Exception {
        // waiting for update trade state
        sleep(SwiftPassConst.LOOP_WAITING_TIME);//等待一定时间再进行查询，避免状态还没来得及被更新

        ReverseRequest reverseRequest = new ReverseRequest(outTradeNo);
        String reverseResultXml = HttpRequest.request(reverseRequest);
        ReverseResult reverseResult = XmlUtils.parseFromXML(reverseResultXml, "xml", ReverseResult.class);
        if (reverseResult == null) {
            // 支付订单撤销请求逻辑错误，请仔细检测传过去的每一个参数是否合法
            return false;
        }
        if (!"0".equals(reverseResult.getStatus())) {
            throw new SwiftPassException("冲正失败");
             // 注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
//            return false;
        }
        if (!"0".equals(reverseResult.getResult_code())) {
            throw new SwiftPassException("冲正失败");
//            // 表示需要重试
//            needRecallReverse = true;
//            return false;
        } else {
            // 表示不需要重试，也可以当作是撤销成功
            needRecallReverse = false;
            return true;
        }
    }
}
