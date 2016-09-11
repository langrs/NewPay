package com.esummer.android.stateupdatehandler;

/**
 * Created by cwj on 16/7/11.
 */
public interface StateUpdateHandlerListener<HandlerType, ResponseType> {

    void onUpdate(String key, HandlerType handler, ResponseType response);

    void onCleanup(String key, HandlerType handler, ResponseType response);
}
