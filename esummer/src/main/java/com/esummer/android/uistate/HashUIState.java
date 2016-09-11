package com.esummer.android.uistate;

import java.util.HashMap;

/**
 * Created by cwj on 16/6/30.
 */
public class HashUIState<K, V> implements UIState<K, V> {
    private HashMap<K, V> map = new HashMap<>();

    @Override
    public V getState(K key) {
        return map.get(key);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void saveState(K key, V value) {
        map.put(key, value);
    }

    @Override
    public V remove(K key) {
        return map.remove(key);
    }
}
