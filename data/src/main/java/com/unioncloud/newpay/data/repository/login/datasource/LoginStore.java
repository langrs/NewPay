package com.unioncloud.newpay.data.repository.login.datasource;

import com.unioncloud.newpay.data.entity.login.LoginResultEntity;

import rx.Observable;

/**
 * Created by cwj on 16/8/8.
 */
public interface LoginStore {

    Observable<LoginResultEntity> login(String userNo, String password, String mac);

    Observable<Boolean> changePassword(String shopId, String userId,
                              String newPassword, String oldPassword);
}
