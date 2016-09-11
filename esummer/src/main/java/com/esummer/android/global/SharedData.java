package com.esummer.android.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.esummer.android.utils.ExecutorUtils;
import com.raizlabs.coreutils.util.observable.lists.ListObserver;
import com.raizlabs.coreutils.util.observable.lists.ObservableList;
import com.raizlabs.coreutils.util.observable.lists.ObservableListWrapper;

import java.util.ListIterator;

/**
 * Created by cwj on 16/7/11.
 */
public class SharedData {
    private static final String PREFERENCE_NAME = "GLOBAL_SHARE";

    private SharedPreferences sharedPreferences;
    private ObservableList<String> observableList;

    public SharedData(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        observableList = new ObservableListWrapper<>();
    }

    private String getKey(int index) {
        return Integer.toString(index);
    }

    private void reload() {
        synchronized (this) {
            observableList.beginTransaction();
            observableList.clear();
            for (int i = 0; i < 50; i ++) {
                String str = sharedPreferences.getString(getKey(i), null);
                if (!TextUtils.isEmpty(str)) {
                    observableList.add(str);
                }
            }
            observableList.endTransaction();
        }
    }

    private void update() {
        ExecutorUtils.loadDataExecutor.submit(new Runnable() {
            @Override
            public void run() {
                updateSync();
            }
        });
    }

    private void updateSync() {
        synchronized (this) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            int savedSize = observableList.size();
            for (int i = 0; i < 50; i ++) {
                if (i < savedSize) {
                    edit.putString(getKey(i), observableList.get(i));
                } else {
                    edit.remove(getKey(i));
                }
            }
            edit.apply();
        }
    }

    public ListObserver<String> getObservableList() {
        return this.observableList.getListObserver();
    }

    public void updateData(String data) {
        synchronized (this) {
            observableList.beginTransaction();
            ListIterator<String> iterator = observableList.listIterator();
            while (iterator.hasNext()) {
                if (iterator.next().equals(data)) {
                    iterator.remove();
                }
            }
            observableList.add(0, data);
            update();
        }
    }

    public void clear() {
        synchronized (this) {
            this.observableList.clear();
            update();
        }
    }
}
