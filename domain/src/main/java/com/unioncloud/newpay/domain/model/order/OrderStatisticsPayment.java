package com.unioncloud.newpay.domain.model.order;

/**
 * Created by cwj on 16/9/3.
 */
public class OrderStatisticsPayment {
    public String paymentId;
    public String paymentName;
    public int payCount;       // 支付的使用次数
    public int payTotals;      // 支付总计
}
