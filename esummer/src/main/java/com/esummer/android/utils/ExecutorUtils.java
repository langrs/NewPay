package com.esummer.android.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by cwj on 16/7/11.
 */
public class ExecutorUtils {

    public static final ExecutorService imageDecodeExecutor;
    public static final ExecutorService loadDataExecutor;

    static {
        ThreadPoolExecutor imagePool = new ThreadPoolExecutor(2, 6, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        imagePool.setThreadFactory(createFactory("Image Decode Thread"));
        imageDecodeExecutor = imagePool;
        ThreadPoolExecutor dataPool = new ThreadPoolExecutor(1, 3, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        dataPool.setThreadFactory(createFactory("General Background Thread"));
        loadDataExecutor = dataPool;
    }

    private static ThreadFactory createFactory(final String tag) {
        return new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable r) {
                return new Thread(new Runnable() {
                    @Override
                    public void run() {
                        android.os.Process.setThreadPriority(10);
                        r.run();
                    }
                }, tag);
            }
        };
    }
}
