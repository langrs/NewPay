package com.unioncloud.newpay.domain.model.order;

import java.util.List;

/**
 * Created by cwj on 16/9/3.
 */
public class OrderStatistics {
    private int statisticsTotal;
    private int saleCount;
    private int saleTotals;
    private int refundCount;
    private int refundTotal;
    private List<OrderStatisticsPayment> payStatistics;

    public int getStatisticsTotal() {
        return statisticsTotal;
    }

    public void setStatisticsTotal(int statisticsTotal) {
        this.statisticsTotal = statisticsTotal;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    public int getSaleTotals() {
        return saleTotals;
    }

    public void setSaleTotals(int saleTotals) {
        this.saleTotals = saleTotals;
    }

    public int getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(int refundCount) {
        this.refundCount = refundCount;
    }

    public int getRefundTotal() {
        return refundTotal;
    }

    public void setRefundTotal(int refundTotal) {
        this.refundTotal = refundTotal;
    }

    public List<OrderStatisticsPayment> getPayStatistics() {
        return payStatistics;
    }

    public void setPayStatistics(List<OrderStatisticsPayment> payStatistics) {
        this.payStatistics = payStatistics;
    }
}
