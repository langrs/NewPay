package com.esummer.android.stateupdatehandler;

import android.util.Log;

import com.esummer.android.uistate.UIState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by cwj on 16/7/11.
 */
public class StateUpdateHandlerManager<Handler> implements UpdateHandlerManager<Handler> {
    private static final String TAG = StateUpdateHandlerManager.class.getSimpleName();

    static class ResponseDelegate<HandlerType, ResponseType> {
        StateUpdateHandlerListener<HandlerType, ResponseType> loadListener;
        ResponseType response;

        private ResponseDelegate() {
        }
    }
    private Map<String, ResponseDelegate<? extends Handler, ?>> cachedResponses = new HashMap<>();


    private <T extends Handler, K> void updateResponseHelper(String key, Handler handler, ResponseDelegate<T, K> binder) {
        this.updateForResponse(key, handler, binder.response, binder.loadListener);
    }

    private static <H, R> void cleanupResponse(String key, H handler, ResponseDelegate<H, R> binder) {
        try {
            binder.loadListener.onCleanup(key, handler, binder.response);
        } catch (ClassCastException e) {
            Log.e(TAG, "Invalid class cast in cleanupResponse", e);
            throw new ClassCastException("Invalid class cast in cleanupResponse. Does your ResponseDelegate type match your handler?");
        }
    }

    private static <H, R> void updateForResponse(String key, H handler, ResponseDelegate<H, R> binder) {
        try {
            binder.loadListener.onUpdate(key, handler, binder.response);
        } catch (ClassCastException e) {
            Log.e(TAG, "Invalid class cast in updateForResponse", e);
            throw new ClassCastException("Invalid class cast in updateForResponse. Does your ResponseDelegate type match your handler?");
        }
    }

    @Override
    public Object getResponse(String key) {
        ResponseDelegate<? extends Handler, ?> responseObserver = getCachedResponses().get(key);
        if (responseObserver == null) {
            return null;
        }
        return responseObserver.response;
    }

    protected Map<String, ResponseDelegate<? extends Handler, ?>> getCachedResponses() {
        return this.cachedResponses;
    }

    @Override
    public void saveState(UIState<String, Object> state) {
        ArrayList<String> keyList = new ArrayList(getCachedResponses().size());
        Iterator<Map.Entry<String, ResponseDelegate<? extends Handler, ?>>> iterator
                = getCachedResponses().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ResponseDelegate<? extends Handler, ?>> entry = iterator.next();
            String str = entry.getKey();
            state.saveState(str, entry.getValue());
            keyList.add(str);
        }
        state.saveState("ResponseKeys", keyList);
    }

    @Override
    public void clearState(Handler handler) {
        Iterator<Map.Entry<String, ResponseDelegate<? extends Handler, ?>>> iterator
                = getCachedResponses().entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, ResponseDelegate<? extends Handler, ?>> entry = iterator.next();
            cleanupResponse(entry.getKey(), handler, (ResponseDelegate)entry.getValue());
        }
    }

    @Override
    public void updateForResponse(Handler handler, UIState<String, Object> state) {
        getCachedResponses().clear();
        if(state != null) {
            ArrayList<String> keyList = (ArrayList<String>)state.getState("ResponseKeys");
            if(keyList != null) {
                Iterator<String> iterator = keyList.iterator();
                while(iterator.hasNext()) {
                    String key = iterator.next();
                    ResponseDelegate binder = (ResponseDelegate)state.getState(key);
                    if(binder != null) {
                        updateForResponse(key, handler, binder);
                    }
                }
            }
        }
    }

    @Override
    public <HandlerType extends Handler, ResponseType> void updateForResponse(
            String key, Handler handler, ResponseType response,
            StateUpdateHandlerListener<HandlerType, ResponseType> loadListener) {
        ResponseDelegate delegate = new ResponseDelegate();
        delegate.response = response;
        delegate.loadListener = loadListener;
        getCachedResponses().put(key, delegate);
        updateForResponse(key, handler, delegate);
    }

    @Override
    public boolean cleanupResponse(String key, Handler handler) {
        ResponseDelegate responseObserver = getCachedResponses().remove(key);
        if(responseObserver != null) {
            cleanupResponse(key, handler, responseObserver);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateForResponse(String key, Handler handler) {
        ResponseDelegate delegate = getCachedResponses().get(key);
        if(delegate != null) {
            updateForResponse(key, handler, delegate);
            return true;
        } else {
            return false;
        }
    }

}
