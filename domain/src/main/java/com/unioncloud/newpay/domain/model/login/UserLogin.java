package com.unioncloud.newpay.domain.model.login;

/**
 * Created by cwj on 16/6/29.
 */
public class UserLogin {
    private String userNumber;
    private String userPassword;
    private String mac;

    public UserLogin() {
    }

    public UserLogin(String userNumber, String userPassword) {
        this.userNumber = userNumber;
        this.userPassword = userPassword;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
