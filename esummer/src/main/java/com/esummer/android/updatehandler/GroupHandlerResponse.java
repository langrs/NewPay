package com.esummer.android.updatehandler;

import com.esummer.android.uistate.HashUIState;
import com.esummer.android.uistate.UIState;

/**
 * Created by cwj on 16/8/8.
 */
public abstract class GroupHandlerResponse<KeyType, DataType, ResponseType extends UpdateHandler<DataType, ?>> {

    private UIState<KeyType, ResponseType> savedUpdate = new HashUIState<>();

    public ResponseType requestUpdate(KeyType key, boolean isUpdating) {
        synchronized (this) {
            ResponseType response = getStatedResponse(key);
            if (response == null) {
                response = createAndStated(key);
                startUpdate(response, true);
            }
            return response;
        }
    }

    protected void notifySuccess(ResponseType response) {
        response.onUpdateCompleted();
    }

    protected void startUpdate(ResponseType response, boolean isUpdating) {
        if (response.startUpdate()) {
            doUpdate(response, isUpdating);
        }
    }

    protected abstract ResponseType createResponse(DataType data, boolean isUpdating);

    protected abstract DataType getDataByResponseKey(KeyType key);

    protected void notifyFailed(ResponseType response) {
        response.onUpdateFailed();
    }

    protected abstract void doUpdate(ResponseType response, boolean isUpdating);

    public void removeResponse(KeyType key) {
        this.savedUpdate.remove(key);
    }

    /** 根据具体数据类型返回唯一表示的key */
    protected abstract KeyType getDateKey(DataType data);

    public ResponseType getStatedResponse(KeyType key) {
        return this.savedUpdate.getState(key);
    }

    public ResponseType getResponse(KeyType key) {
        synchronized (getStatusLock()) {
            ResponseType response = getStatedResponse(key);
            if (response == null) {
                response = createAndStated(key);
            }
            return response;
        }
    }

    public void clear() {
        this.savedUpdate.clear();
    }

    protected ResponseType createAndStated(KeyType key) {
        ResponseType response = createResponse(getDataByResponseKey(key), false);
        this.savedUpdate.saveState(key, response);
        return response;
    }

    public Object getStatusLock() {
        return this;
    }
}
