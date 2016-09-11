package com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass;

import com.unioncloud.newpay.data.repository.thirdparty.datasource.swiftpass.query.QueryResult;

/**
 * Created by cwj on 16/7/21.
 */
public interface QueryPayListener {
    void onFail();

    void onSuccess(QueryResult data);
}
