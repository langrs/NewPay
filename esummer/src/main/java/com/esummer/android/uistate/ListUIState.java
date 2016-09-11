package com.esummer.android.uistate;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by cwj on 16/6/30.
 */
public class ListUIState<K, V> implements UIState<K, V> {

    private UIState<K, V> retain;
    private List<K> keyList;

    public ListUIState(UIState<K, V> retain) {
        this.retain = retain;
        keyList = new LinkedList<>();
    }

    @Override
    public V getState(K key) {
        return retain.getState(key);
    }

    @Override
    public void clear() {
        keyList.clear();
        retain.clear();
    }

    @Override
    public void saveState(K key, V value) {
        keyList.add(key);
        retain.saveState(key, value);
    }

    public void setKeyList(Collection<? extends K> keys) {
        keyList.addAll(keys);
    }

    @Override
    public V remove(K key) {
        K temp = null;
        for(ListIterator<K> iterator = keyList.listIterator();
                iterator.hasNext();
            temp = iterator.next()) {
            if (temp == null && key == null || temp != null && temp.equals(key)) {
                iterator.remove();
            }
        }
        return retain.remove(key);
    }

    public List<K> getKeyList() {
        return keyList;
    }

    public void removeAll() {
        Iterator<K> keyIterator = keyList.iterator();
        while(keyIterator.hasNext()) {
            K k = keyIterator.next();
            retain.remove(k);
        }
        keyList.clear();
    }

}
