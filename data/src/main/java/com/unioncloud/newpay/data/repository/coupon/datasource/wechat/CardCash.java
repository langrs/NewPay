package com.unioncloud.newpay.data.repository.coupon.datasource.wechat;

public class CardCash {
//    消费限额
    private Integer least_cost;
//    面值
    private Integer reduce_cost;

    public Integer getLeast_cost() {
        return least_cost;
    }

    public void setLeast_cost(Integer least_cost) {
        this.least_cost = least_cost;
    }

    public Integer getReduce_cost() {
        return reduce_cost;
    }

    public void setReduce_cost(Integer reduce_cost) {
        this.reduce_cost = reduce_cost;
    }
}