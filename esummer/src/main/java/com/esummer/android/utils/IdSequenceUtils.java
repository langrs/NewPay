package com.esummer.android.utils;

/**
 * Created by cwj on 16/7/11.
 */
public class IdSequenceUtils {

    private static int idSequence = 0;

    public static int nextId() {
        int nextId = idSequence;
        idSequence = nextId + 1;
        return nextId;
    }

    /**
     * Called when the {@code newId} is not made by {@link IdSequenceUtils#nextId()}.
     * Make sure the id sequence not create equals id
     * @param newId
     */
    public static void updateSequence(int newId) {
        idSequence = Math.max(idSequence, newId);
    }
}
