package com.unioncloud.newpay.domain.model.thirdparty;

import java.util.List;

/**
 * Created by cwj on 16/8/18.
 */
public class ThirdPartyRefundRecord {
    private String orderId;         // 原订单号
    private String thirdOrderId;    // 原第三方订单号
    private int refundCount;     // 退款次数

    private List<ThirdPartyRefundOrder> refundOrderList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getThirdOrderId() {
        return thirdOrderId;
    }

    public void setThirdOrderId(String thirdOrderId) {
        this.thirdOrderId = thirdOrderId;
    }

    public int getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(int refundCount) {
        this.refundCount = refundCount;
    }

    public List<ThirdPartyRefundOrder> getRefundOrderList() {
        return refundOrderList;
    }

    public void setRefundOrderList(List<ThirdPartyRefundOrder> refundOrderList) {
        this.refundOrderList = refundOrderList;
    }
}
