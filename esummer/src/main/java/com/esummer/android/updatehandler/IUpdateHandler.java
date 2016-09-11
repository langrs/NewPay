package com.esummer.android.updatehandler;

/**
 * 异步数据. DataType表示数据类型,ResponseType表示update数据的响应
 */
public interface IUpdateHandler<DataType, ResponseType extends IUpdateHandler<?, ?>> {

}
