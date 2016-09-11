package com.esummer.android.task;

import com.raizlabs.coreutils.threading.ThreadingUtils;

/**
 * Created by cwj on 16/6/30.
 */
public class UITaskQueue extends TaskQueue {

    @Override
    protected void dispatchOnExecutor(Runnable runnable) {
        if (ThreadingUtils.isOnUIThread()) {
            runnable.run();
        } else {
            ThreadingUtils.runOnUIThread(runnable);
        }
    }
}
