package com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass;

import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.pay.PayResult;

/**
 * Created by cwj on 16/7/21.
 */
public interface PayListener {

    void onSuccess(PayResult payResult);

    void onFail(String cause);

}
