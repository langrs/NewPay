package com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.query;

import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.HttpRequest;
import com.unioncloud.newpay.data.utils.XmlUtils;

/**
 * Created by cwj on 16/8/18.
 */
public class PayQueryService {

    public QueryResult query(String outTradeNo, String transactionId)  throws Exception {
        QueryRequest request = new QueryRequest(outTradeNo, transactionId);
        String queryResultXml = HttpRequest.request(request);
        QueryResult queryResult = XmlUtils.parseFromXML(queryResultXml, "xml", QueryResult.class);
        return queryResult;
    }

}
