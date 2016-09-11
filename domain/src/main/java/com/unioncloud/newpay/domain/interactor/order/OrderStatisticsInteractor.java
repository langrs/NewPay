package com.unioncloud.newpay.domain.interactor.order;

import com.unioncloud.newpay.domain.executor.ExecutorProvider;
import com.unioncloud.newpay.domain.interactor.BaseInteractor;
import com.unioncloud.newpay.domain.model.order.OrderStatistics;
import com.unioncloud.newpay.domain.model.order.OrderStatisticsPayment;
import com.unioncloud.newpay.domain.model.order.QuerySaleOrderCommand;
import com.unioncloud.newpay.domain.model.order.SaleOrder;
import com.unioncloud.newpay.domain.model.order.SaleOrderHeader;
import com.unioncloud.newpay.domain.model.payment.PaymentUsed;
import com.unioncloud.newpay.domain.repository.SaleOrderRepository;
import com.unioncloud.newpay.domain.utils.MoneyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by cwj on 16/9/4.
 */
public class OrderStatisticsInteractor extends BaseInteractor<OrderStatistics> {

    private SaleOrderRepository repository;
    private QuerySaleOrderCommand command;

    public OrderStatisticsInteractor(ExecutorProvider provider,
                                     QuerySaleOrderCommand command,
                                     SaleOrderRepository repository) {
        super(provider);
        this.command = command;
        this.repository = repository;
    }

    @Override
    protected Observable<OrderStatistics> bindObservable() {
        return repository.querySaleOrder(command)
                .map(new Func1<List<SaleOrder>, OrderStatistics>() {
                    @Override
                    public OrderStatistics call(List<SaleOrder> saleOrders) {
                        return mapper(saleOrders);
                    }
                });
    }

    private static OrderStatistics mapper(List<SaleOrder> orderList) {
        if (orderList == null) {
            return null;
        }
        int saleTotals = 0;
        int saleCount = 0;
//        HashMap<String, OrderStatisticsPayment> salePayTotals = new HashMap<>();

        int refundTotals = 0;
        int refundCount = 0;
//        HashMap<String, OrderStatisticsPayment> refundPayTotals = new HashMap<>();
        HashMap<String, OrderStatisticsPayment> payTotals = new HashMap<>();


        for (SaleOrder order : orderList) {
            SaleOrderHeader header = order.getHeader();
            if ("01".equals(header.getDealType())) {
                saleTotals += MoneyUtils.getFen(header.getPayAmount());
                saleCount++;
                List<PaymentUsed> usedList = order.getPaymentUsedList();
                for (PaymentUsed used : usedList) {
                    String paymentId = used.getPaymentId();
                    OrderStatisticsPayment payStatistics;
                    if (payTotals.containsKey(paymentId)) {
                        payStatistics = payTotals.get(paymentId);
                    } else {
                        payStatistics = new OrderStatisticsPayment();
                        payStatistics.paymentId = paymentId;
                        payTotals.put(paymentId, payStatistics);
                    }
                    payStatistics.payCount += 1;
                    payStatistics.payTotals += used.getPayAmount();
                }
            } else if ("02".equals(header.getDealType())) {
                refundTotals += MoneyUtils.getFen(header.getPayAmount());
                refundCount++;
                List<PaymentUsed> usedList = order.getPaymentUsedList();
                for (PaymentUsed used : usedList) {
                    String paymentId = used.getPaymentId();
                    OrderStatisticsPayment payStatistics;
                    if (payTotals.containsKey(paymentId)) {
                        payStatistics = payTotals.get(paymentId);
                    } else {
                        payStatistics = new OrderStatisticsPayment();
                        payStatistics.paymentId = paymentId;
                        payTotals.put(paymentId, payStatistics);
                    }
                    payStatistics.payCount += 1;
                    payStatistics.payTotals += used.getPayAmount();
                }
            }
        }

        OrderStatistics saleStatistics = new OrderStatistics();
        saleStatistics.setSaleCount(saleCount);
        saleStatistics.setSaleTotals(saleTotals);
        saleStatistics.setRefundCount(refundCount);
        saleStatistics.setRefundTotal(refundTotals);
        saleStatistics.setStatisticsTotal(saleTotals + refundTotals);
        ArrayList<OrderStatisticsPayment> salePayStatistics = new ArrayList<>(payTotals.values());
        saleStatistics.setPayStatistics(salePayStatistics);

        return saleStatistics;
    }
}
