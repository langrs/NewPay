package com.esummer.android.updatehandler;

/**
 * Created by cwj on 16/8/7.
 */
public interface UpdateSuccessFailedCallback<ResponseType extends IUpdateHandler<?, ?>> {

    void onSuccess(ResponseType response);

    void onFailed(ResponseType response);

}
