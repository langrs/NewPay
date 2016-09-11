package com.unioncloud.newpay.data.repository.bankcardpay.datasource;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.google.gson.JsonSyntaxException;
import com.pax.bankcardpay.aidl.BankCardPay;
import com.unioncloud.newpay.data.exception.RemoteDataException;
import com.unioncloud.newpay.data.utils.JsonUtils;
import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundRequest;
import com.unioncloud.newpay.domain.model.backcardpay.refund.BankcardRefundResult;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleRequest;
import com.unioncloud.newpay.domain.model.backcardpay.sale.BankcardSaleResult;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * Created by cwj on 16/8/17.
 */
public class PaxBankcardPayStore implements BankcardPayStore {

    private static final String ACTION_PAY = "com.pax.bankcardpay.service.BANKCARDPAY_SERVICE";
    private BankCardPay bankCardPay;

    private Context context;

    public PaxBankcardPayStore(Context context) {
        this.context = context.getApplicationContext();
    }

    private ServiceConnection serviceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            bankCardPay = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bankCardPay = BankCardPay.Stub.asInterface(service);
        }
    };

    private void bindService() {
        try {
            context.bindService(new Intent(ACTION_PAY), serviceConn, Context.BIND_AUTO_CREATE);
        } catch (SecurityException e) {
        }
    }

    @Override
    public Observable<BankcardSaleResult> sale(final BankcardSaleRequest request) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    int count = 0;
                    while(bankCardPay == null) {
                        if (count > 3) {
                            subscriber.onError(new RemoteDataException("银联支付调用异常"));
                            return;
                        }
                        bindService();
                        Thread.sleep(500);
                        count++;
                    }
                    String resultJson = bankCardPay.sale(JsonUtils.toJson(new PaxSaleRequest(request)));
                    if (resultJson != null && !"".equals(resultJson)) {
                        subscriber.onNext(resultJson);
                    } else {
                        subscriber.onError(new RemoteDataException("银联支付返回数据异常"));
                    }
                } catch (InterruptedException e) {
                    subscriber.onError(new RemoteDataException("银联支付调用异常"));
                } catch (RemoteException e) {
                    subscriber.onError(new RemoteDataException("银联支付调用异常"));
                }
            }
        }).map(new Func1<String, BankcardSaleResult>() {
            @Override
            public BankcardSaleResult call(String s) {
                try {
                    BankcardSaleResult result = JsonUtils.fromJson(s, BankcardSaleResult.class);
                    if (result == null || !"00".equals(result.getRespCode())) {
                        String errorMessage = getErrorMessageByCode(result.getRespCode());
                        if (errorMessage == null) {
                            errorMessage = result.getRespMessage();
                        }
                        throw Exceptions.propagate(new RemoteDataException(errorMessage));
                    }
                    result.setAmount(request.getTransAmount());
                    return result;
                } catch (JsonSyntaxException e) {
                    throw Exceptions.propagate(new RemoteDataException("解析服务接口数据失败"));
                }
            }
        });
    }

    private static String getErrorMessageByCode(String errorCode) {
        if ("Z1".equals(errorCode)) {
            return "传递过来的数据为空";
        } else if ("Z2".equals(errorCode)) {
            return "应用ID为空";
        } else if ("Z3".equals(errorCode)) {
            return "交易取消";
        } else if ("Z4".equals(errorCode)) {
            return "接受失败";
        } else if ("Z5".equals(errorCode)) {
            return "抱歉,该应用不是当前签到应用";
        } else if ("Z6".equals(errorCode)) {
            return "抱歉,该操作员不是当前签到操作员号";
        } else if ("Z7".equals(errorCode)) {
            return "交易失败";
        } else if ("B0".equals(errorCode)) {
            return "帐号验证成功";
        } else if ("B1".equals(errorCode)) {
            return "操作员帐号不存在";
        } else if ("B2".equals(errorCode)) {
            return "操作员不能为空";
        } else if ("B3".equals(errorCode)) {
            return "操作员帐号长度有误";
        } else if ("B4".equals(errorCode)) {
            return "操作原密码不能为空";
        } else if ("B5".equals(errorCode)) {
            return "操作员密码错误";
        } else if ("B6".equals(errorCode)) {
            return "POS机终端未签到";
        } else if ("B9".equals(errorCode)) {
            return "操作员未签到";
        } else if ("C1".equals(errorCode)) {
            return "金额格式错误";
        } else if ("C2".equals(errorCode)) {
            return "金额不能为空";
        } else if ("C3".equals(errorCode)) {
            return "金额超限";
        } else if ("I1".equals(errorCode)) {
            return "原参考号不能为空";
        } else if ("I2".equals(errorCode)) {
            return "原参考号错误";
        } else if ("I3".equals(errorCode)) {
            return "日期长度有误";
        } else if ("I4".equals(errorCode)) {
            return "日期格式有误";
        }
        return null;
    }

    @Override
    public Observable<BankcardRefundResult> saleVoid(final BankcardRefundRequest request) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    int count = 0;
                    while(bankCardPay == null) {
                        if (count > 3) {
                            subscriber.onError(new RemoteDataException("银联支付调用异常"));
                            return;
                        }
                        bindService();
                        Thread.sleep(500);
                        count++;
                    }
                    String resultJson = bankCardPay.saleVoid(JsonUtils.toJson(new PaxSaleVoidRequest(request)));
                    if (resultJson != null && !"".equals(resultJson)) {
                        subscriber.onNext(resultJson);
                    } else {
                        subscriber.onError(new RemoteDataException("银联支付返回数据异常"));
                    }
                } catch (InterruptedException e) {
                    subscriber.onError(new RemoteDataException("银联支付调用异常"));
                } catch (RemoteException e) {
                    subscriber.onError(new RemoteDataException("银联支付调用异常"));
                }
            }
        }).map(new Func1<String, BankcardRefundResult>() {
            @Override
            public BankcardRefundResult call(String s) {
                try {
                    BankcardRefundResult result = JsonUtils.fromJson(s, BankcardRefundResult.class);
                    if (result == null || !"00".equals(result.getRespCode())) {
                        throw Exceptions.propagate(new RemoteDataException(result.getRespMessage()));
                    }
                    result.setAmount(request.getTransAmount());
                    return result;
                } catch (JsonSyntaxException e) {
                    throw Exceptions.propagate(new RemoteDataException("解析服务接口数据失败"));
                }
            }
        });
    }

    @Override
    public Observable<BankcardRefundResult> refund(final BankcardRefundRequest request) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    int count = 0;
                    while(bankCardPay == null) {
                        if (count > 3) {
                            subscriber.onError(new RemoteDataException("银联支付调用异常"));
                            return;
                        }
                        bindService();
                        Thread.sleep(500);
                        count++;
                    }
                    String resultJson = bankCardPay.refund(JsonUtils.toJson(new PaxRefundRequest(request)));
                    if (resultJson != null && !"".equals(resultJson)) {
                        subscriber.onNext(resultJson);
                    } else {
                        subscriber.onError(new RemoteDataException("银联支付返回数据异常"));
                    }
                } catch (InterruptedException e) {
                    subscriber.onError(new RemoteDataException("银联支付调用异常"));
                } catch (RemoteException e) {
                    subscriber.onError(new RemoteDataException("银联支付调用异常"));
                }
            }
        }).map(new Func1<String, BankcardRefundResult>() {
            @Override
            public BankcardRefundResult call(String s) {
                try {
                    BankcardRefundResult result = JsonUtils.fromJson(s, BankcardRefundResult.class);
                    if (!"A".equals(result.getRespCode())) {
                        throw Exceptions.propagate(new RemoteDataException(result.getRespMessage()));
                    }
                    result.setAmount(request.getTransAmount());
                    return result;
                } catch (JsonSyntaxException e) {
                    throw Exceptions.propagate(new RemoteDataException("解析服务接口数据失败"));
                }
            }
        });
    }

}
