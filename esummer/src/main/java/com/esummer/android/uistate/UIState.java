package com.esummer.android.uistate;

/**
 * The class for UI Save/Restore state
 */
public interface UIState<K, V> {

    V getState(K key);

    void clear();

    void saveState(K key, V state);

    V remove(K key);
}
