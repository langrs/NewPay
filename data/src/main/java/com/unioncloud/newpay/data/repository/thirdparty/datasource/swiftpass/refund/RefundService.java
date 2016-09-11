package com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.refund;

import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.HttpRequest;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.Signature;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.SwiftPassConst;
import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.SwiftPassException;
import com.unioncloud.newpay.data.utils.XmlUtils;

/**
 * Created by cwj on 16/8/18.
 */
public class RefundService {

    public RefundResult refundBusiness(RefundRequest request) throws Exception {
        String resultXml = HttpRequest.request(request);
        RefundResult refundResult = XmlUtils.parseFromXML(resultXml, "xml", RefundResult.class);
        if (refundResult == null || refundResult.getStatus() == null) {
            // "Case1:退款API请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问"
            throw new SwiftPassException("参数错误1");
        }
        if (!"0".equals(refundResult.getStatus())) {
            throw new SwiftPassException("参数错误2");
        }
        if (!Signature.checkXmlSign(resultXml, SwiftPassConst.KEY)) {
            throw new SwiftPassException("验证签名失败");
        }
        if ("0".equals(refundResult.getResult_code())) {   // 退款成功
            return refundResult;
        } else {
            throw new SwiftPassException("退款失败");
        }
    }

}
