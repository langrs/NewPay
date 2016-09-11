package com.esummer.android.stateupdatehandler;

import com.esummer.android.uistate.UIState;

/**
 * The class use to save the response that is requested by a async request.
 */
public interface UpdateHandlerManager<Handler> {

    /**
     * Returns stated response
     * @param key
     * @return
     */
    Object getResponse(String key);

    /**
     * Save all the response state in UIState
     * @param state
     */
    void saveState(UIState<String, Object> state);

    /**
     * Clear all the response state
     * @param handler
     */
    void clearState(Handler handler);

    /**
     *
     * @param handler
     * @param state
     */
    void updateForResponse(Handler handler, UIState<String, Object> state);

    <HandlerType extends Handler, ResponseType> void updateForResponse(
            String key, Handler handler,
            ResponseType responseType, StateUpdateHandlerListener<HandlerType, ResponseType> loadListener);

    boolean cleanupResponse(String key, Handler handler);

    boolean updateForResponse(String key, Handler handler);

}
