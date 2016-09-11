package com.esummer.android.updatehandler;

/**
 * Created by cwj on 16/8/8.
 */
public class SimpleUpdateHandler<DataType> extends UpdateHandler<DataType, SimpleUpdateHandler<DataType>> {

    SimpleUpdateHandler(DataType data, boolean isUpdating) {
        super(data, isUpdating);
    }
}
