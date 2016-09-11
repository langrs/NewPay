package com.esummer.android.updatehandler;

/**
 * Created by cwj on 16/8/8.
 */
public abstract class SimpleGroupUpdateHandler<KeyType, DataType>
        extends GroupHandlerResponse<KeyType, DataType, SimpleUpdateHandler<DataType>> {

    @Override
    protected SimpleUpdateHandler<DataType> createResponse(DataType data, boolean isUpdating) {
        return new SimpleUpdateHandler<>(data, isUpdating);
    }

    @Override
    protected void doUpdate(SimpleUpdateHandler<DataType> response, boolean isUpdating) {
        notifySuccess(response);
    }

}
