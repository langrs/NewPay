package com.esummer.android.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public abstract class TaskQueue {

    private final List<Runnable> queue = new ArrayList<>();

    private boolean isPaused;

    public synchronized void loop() {
        isPaused = false;
        Iterator<Runnable> iterator = queue.iterator();
        while (iterator.hasNext()) {
            execute(iterator.next());
            iterator.remove();
        }
    }

    public synchronized void execute(Runnable runnable) {
        if(isPaused) {
            if(isEffective(runnable)) {
                postToQueue(runnable);
            }
        } else {
            dispatchOnExecutor(runnable);
        }
    }

    public synchronized void pause() {
        isPaused = true;
    }

    protected boolean isEffective(Runnable runnable) {
        return true;
    }

    protected void postToQueue(Runnable runnable) {
        queue.add(runnable);
    }

    protected abstract void dispatchOnExecutor(Runnable runnable);
}
