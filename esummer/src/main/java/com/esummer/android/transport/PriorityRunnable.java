package com.esummer.android.transport;

import com.raizlabs.coreutils.concurrent.Prioritized;

/**
 * Created by cwj on 16/7/18.
 */
abstract class PriorityRunnable implements Prioritized, Comparable<PriorityRunnable>, Runnable {
    private int priority;

    public PriorityRunnable(int priority) {
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(PriorityRunnable another) {
        return COMPARATOR_HIGH_FIRST.compare(this, another);
    }
}