package com.esummer.android.updatehandler;

/**
 * 数据更新完成的回调接口.响应完成, 不代表响应成功
 */
public interface UpdateCompleteCallback<ResponseType extends IUpdateHandler<?, ?>> {

    /**
     *
     * @param response 数据更新的响应
     * @param isSuccess true 表示数据更新成功
     */
    void onCompleted(ResponseType response, boolean isSuccess);

}
