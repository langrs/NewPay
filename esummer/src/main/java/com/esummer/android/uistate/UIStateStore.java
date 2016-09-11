package com.esummer.android.uistate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * This class us
 */
public class UIStateStore extends Fragment {
    private static HashUIState<String, Object> tracking = new HashUIState<>();

    private ListUIState<String, Object> retainData;

    public UIStateStore() {
        setArguments(new Bundle());
    }

    public ListUIState<String, Object> getRetainData() {
        return retainData;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        retainData = new ListUIState<>(tracking);
        if (savedInstanceState != null) {
            ArrayList<String> keyList = savedInstanceState.getStringArrayList("Tracking:AddedKeys");
            if (keyList != null) {
                retainData.setKeyList(keyList);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList("Tracking:AddedKeys",
                new ArrayList<String>(retainData.getKeyList()));
    }
}
