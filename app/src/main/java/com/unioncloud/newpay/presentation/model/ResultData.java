package com.unioncloud.newpay.presentation.model;

/**
 * Created by cwj on 16/8/9.
 */
public class ResultData<DataType> {
    private boolean isSuccess;
    private DataType data;
    private String errorMessage;

    public ResultData() {
        this(false, null, null);
    }

    public ResultData(boolean isSuccess, DataType data, String errorMessage) {
        this.isSuccess = isSuccess;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public DataType getData() {
        return data;
    }

    public void onSuccess(DataType data) {
        this.data = data;
        isSuccess = true;
    }

    public void onFailed(String errorMessage) {
        this.errorMessage = errorMessage;
        isSuccess = false;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
