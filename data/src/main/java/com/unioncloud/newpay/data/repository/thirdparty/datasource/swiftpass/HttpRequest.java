package com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass;

//import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.pay.PayRequest;
//import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.pay.PayResult;
//import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.query.QueryRequest;
//import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.query.QueryResult;
//import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.reverse.ReverseRequest;
//import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.reverse.ReverseResult;
import com.unioncloud.newpay.data.utils.XmlUtils;

import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by cwj on 16/7/21.
 */
public class HttpRequest {

//    public static PayResult payRequest(PayRequest request) {
//        String xml = request(request);
//        return XmlUtils.parseFromXML(xml, "xml", PayResult.class);
//    }
//
//    public static QueryResult queryRequest(QueryRequest request) {
//        String xml = request(request);
//        return XmlUtils.parseFromXML(xml, "xml", QueryResult.class);
//    }
//
//    public static ReverseResult reverseRequest(ReverseRequest request) {
//        String xml = request(request);
//        return XmlUtils.parseFromXML(xml, "xml", ReverseResult.class);
//    }

    public static String request(Object obj) {
        String xml = XmlUtils.createXml(obj);

        RequestParams params = new RequestParams(SwiftPassConst.URL);
//        params.setSslSocketFactory(sslContext.getSocketFactory());
        params.setConnectTimeout(SwiftPassConst.TIMEOUT);
        params.setMaxRetryCount(0);
        params.setCharset("UTF-8");
        params.setBodyContent(xml);

        try {
            String result = x.http().postSync(params, String.class);
            return result;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
