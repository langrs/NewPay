package com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.refund;

import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.HttpRequest;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.Signature;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.SwiftPassConst;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.SwiftPassException;
import com.unioncloud.newpay.data.utils.Util;
import com.unioncloud.newpay.data.utils.XMLParser;
import com.unioncloud.newpay.data.utils.XmlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cwj on 16/8/18.
 */
public class RefundQueryService {

    public RefundQueryResult queryBusiness(RefundQueryRequest request) throws Exception {
        String resultXml = HttpRequest.request(request);
        RefundQueryResult result = XmlUtils.parseFromXML(resultXml, "xml", RefundQueryResult.class);
        if (result == null || result.getStatus() == null) {
            // "Case1:退款API请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问"
            throw new SwiftPassException("参数错误1");
        }
        if (!"0".equals(result.getStatus())) {
            throw new SwiftPassException("参数错误2");
        }
        if (!Signature.checkXmlSign(resultXml, SwiftPassConst.KEY)) {
            throw new SwiftPassException("验证签名失败");
        }
        if ("0".equals(result.getResult_code())) {
            result.setRefundOrderList(getRefundOrderList(resultXml));
            return result;
        } else {
            throw new SwiftPassException("查询退款失败:" + result.getMessage());
        }
    }

    private List<RefundOrderData> getRefundOrderList(String xml) throws Exception{
        List list = new ArrayList();

        Map<String,Object> map = null;
        map = XMLParser.getMapFromXML(xml);

        int count = Util.getIntFromMap(map, "refund_count");
        if(count == 0){
            return list;
        }

        RefundOrderData refundOrderData;
        for(int i=0;i<count;i++){
            refundOrderData = new RefundOrderData();
            refundOrderData.setOutRefundNo(Util.getStringFromMap(map,"out_refund_no_" + i,""));
            refundOrderData.setRefundID(Util.getStringFromMap(map,"refund_id_" + i,""));
            refundOrderData.setRefundChannel(Util.getStringFromMap(map,"refund_channel_" + i,""));
            refundOrderData.setRefundFee(Util.getIntFromMap(map,"refund_fee_" + i));
            refundOrderData.setCouponRefundFee(Util.getIntFromMap(map,"coupon_refund_fee_" + i));
            refundOrderData.setRefundStatus(Util.getStringFromMap(map,"refund_status_" + i,""));
            refundOrderData.setRefundTime(Util.getStringFromMap(map,"refund_time_" + i,""));
            list.add(refundOrderData);
        }
        return list;
    }
}
