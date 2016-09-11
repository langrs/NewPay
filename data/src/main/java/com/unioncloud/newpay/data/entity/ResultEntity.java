package com.unioncloud.newpay.data.entity;

/**
 * Created by cwj on 16/8/12.
 */
public class ResultEntity<T> {

    protected String status;
    protected String errorMsg;
    protected T data;

    public boolean isSuccess() {
        return "0".equals(status);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
