package com.unioncloud.newpay.presentation.presenter;

import com.esummer.android.updatehandler.UpdateHandler;
import com.unioncloud.newpay.presentation.model.ResultData;

/**
 * Created by cwj on 16/8/9.
 */
public class BooleanUpdateHandler extends UpdateHandler<Boolean, BooleanUpdateHandler> {

    private ResultData<Boolean> result;

    protected BooleanUpdateHandler(boolean isUpdating) {
        super(Boolean.FALSE, isUpdating);
    }

    public String getErrorMessage() {
        if (result != null) {
            return result.getErrorMessage();
        }
        return null;
    }

}
