package com.unioncloud.newpay.domain.repository;

import com.unioncloud.newpay.domain.model.login.LoginResult;
import com.unioncloud.newpay.domain.model.login.UserLogin;

import rx.Observable;

/**
 * Created by cwj on 16/8/8.
 */
public interface LoginRepository {

    Observable<LoginResult> login(UserLogin userLogin);

    Observable<Boolean> changePassword(String shopId, String userId,
                                       String newPassword, String oldPassword);
}
