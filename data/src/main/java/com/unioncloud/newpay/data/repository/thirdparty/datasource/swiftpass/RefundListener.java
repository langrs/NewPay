package com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass;

import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.refund.RefundResult;

/**
 * Created by cwj on 16/7/21.
 */
public interface RefundListener {

    void onSuccess(RefundResult refundResult);

    void onFail(String cause);
}
