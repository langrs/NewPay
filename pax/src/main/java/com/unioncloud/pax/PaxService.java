package com.unioncloud.pax;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.WorkerThread;

import com.google.gson.JsonParseException;
import com.pax.pay.PayHelper;

/**
 * 百富支付服务. 使用时应该保证该对象全局唯一.
 */
public class PaxService {
    private static final String ACTION_PAY = "com.pax.pay.SERVICE";
    PayHelper payHelper;
    Context context;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            payHelper = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            payHelper = PayHelper.Stub.asInterface(service);
        }
    };

    public PaxService(Context context) {
        this.context = context.getApplicationContext();
    }

    private boolean hasBind() {
        return payHelper != null;
    }

    private void bindService() {
        try {
            context.bindService(new Intent(ACTION_PAY), conn, Context.BIND_AUTO_CREATE);
        } catch (SecurityException e) {
        }
    }

    private boolean syncBindService() {
        if (hasBind()) {
            return true;
        }
        try {
            for (int repeatCount = 0; !hasBind() && (repeatCount <= 3); repeatCount++){
                bindService();
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return hasBind();
    }

    /**
     * 消费.
     * @param request
     * @return
     * @throws PaxPayException
     */
    @WorkerThread
    public PaxResponse sale(PaxSaleRequest request) throws PaxPayException {
        return transaction(request);
    }

    /**
     * (当日)消费撤销
     * @param request
     * @return
     * @throws PaxPayException
     */
    @WorkerThread
    public PaxResponse saleVoid(PaxSaleVoidRequest request) throws PaxPayException {
        return transaction(request);
    }

    /**
     * (隔日)退货
     * @param request
     * @return
     * @throws PaxPayException
     */
    @WorkerThread
    public PaxResponse refund(PaxRefundRequest request) throws PaxPayException {
        return transaction(request);
    }

    private PaxResponse transaction(PaxRequest request) throws PaxPayException {
        try {
            String responseJson = payHelper.doTrans(JsonUtils.toJson(request));
            PaxResponse response = JsonUtils.simpleFromJson(responseJson, PaxResponse.class);
            // 将返回状态描述信息替换成PaxResponseCode中预定义的信息. 避免返回英文, 或者乱码
            response.setRspMsg(context.getString(
                    PaxResponseCode.matchByCode(response.getRspCode()).description));
            return response;
        } catch (RemoteException e) {
            throw new PaxPayException(R.string.Pax_Exception_ServiceError);
        } catch (JsonParseException e) {
            throw new PaxPayException(R.string.Pax_Exception_ServiceJSonError);
        }
    }
}
