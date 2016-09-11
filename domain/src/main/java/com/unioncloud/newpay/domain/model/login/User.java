package com.unioncloud.newpay.domain.model.login;

import java.io.Serializable;

/**
 * Created by cwj on 16/6/29.
 */
public class User implements Serializable {
    private String userId;
    private String userNumber;
    private String userName;

    public User() {
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
