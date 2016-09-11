// BankCardPay.aidl
package com.pax.bankcardpay.aidl;

// Declare any non-default types here with import statements

interface BankCardPay {
	String getPosInfo(String request);
	String login(in String request); // 操作员签到
	String logout(in String request); // 操作员签退
	String sale(String request); // 消费
	String saleVoid(String request); // 消费撤销
	String refund(String request); // 退货
	String balance(String request); // 余额查询
	String auth(String request); // 预授权
	String authVoid(String request); // 预授权撤销
	String authCm(String request); // 预授权完成请求
	String authCmVoid(String request); // 预授权完成请求撤销
	String printLast(String request); // 打印最后一笔
	String printAny(String request); // 打印任意一笔
	String printTransDetail(String request); // 打印交易明细
	String printTotal(String request); //  打印交易汇总
	String printLastBatch(String request); //  打印上批总计
	String settle(in String request); // 结算
	String managerOper(String request);//操作员管理
	String ecSale(String request);//电子现金
	String getCardNo(String enterCardMode);//获取银行卡卡号
}
