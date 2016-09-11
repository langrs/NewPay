package com.esummer.android.transport;

import android.text.TextUtils;
import android.util.Log;

import com.raizlabs.coreutils.io.IOUtils;
import com.raizlabs.coreutils.listeners.ProgressListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by cwj on 16/7/4.
 */
public abstract class BaseResponse implements TransportResponse {

    @Override
    public String getStringData() {
        InputStream in = null;
        try {
            in = getInputStreamData();
        } catch (IOException e) {
            Log.w(BaseResponse.class.getSimpleName(), "IOException in getContentAsString: ", e);
        }
        if (in != null) {
            return IOUtils.readStream(in);
        }
        return null;
    }

    @Override
    public boolean getFileData(File target, ProgressListener listener) {
        InputStream input = null;
        try {
            input = getInputStreamData();
        } catch (IOException e) {
            Log.w(BaseResponse.class.getSimpleName(), "IOException in getContentToFile: ", e);
        }
        if (input == null) {
            return false;
        }
        if (!target.exists()) {
            target.getParentFile().mkdirs();
        } else {
            target.delete();
        }
        try {
            FileOutputStream output = new FileOutputStream(target);
            long length = getContentLength();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
                output.flush();
                listener.onProgressUpdate(bytesRead, length);
            }
            return true;
        } catch (IOException e) {
            Log.w(BaseResponse.class.getSimpleName(), "IOException in readContentToFile: ", e);
        }
        return false;
    }

    @Override
    public JSONObject getJsonData() {
        String json = getStringData();
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                return jsonObject;
            } catch (JSONException e) {
                Log.w(BaseResponse.class.getSimpleName(), "Error get json data ", e);
            }
        }
        return null;
    }
}
