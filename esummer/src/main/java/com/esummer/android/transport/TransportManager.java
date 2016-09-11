package com.esummer.android.transport;

import com.esummer.android.transport.ssl.DefaultSocketFactory;
import com.esummer.android.transport.ssl.SSLUtils;
import com.esummer.android.transport.ssl.SSLVerifier;

import java.util.Date;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;

/**
 * Created by cwj on 16/7/15.
 */
public class TransportManager {

    public interface OnCompleteListener<T> {

        void onComplete(TransportManager manager, TransportResult<T> result);

    }

    private EngineFactory engineFactory;
    private ThreadPoolExecutor executor;
    private SocketFactory socketFactory;
    private Semaphore semaphore;
    private int permits;        // http 最大连接数
    private int connectTimeout = 10 * 1000;
    private int soTimeout = 10 * 1000;

    public TransportManager(EngineFactory engineFactory) {
        initFactory(engineFactory);
        initExecutor(5);
    }

    private void initFactory(EngineFactory engineFactory) {
        this.engineFactory = engineFactory;
        setConnectTimeout(getConnectTimeout());
        setSoTimeout(getSoTimeout());
    }

    public EngineFactory getEngineFactory() {
        return engineFactory;
    }

    public void setConnectTimeout(int timeout) {
        this.connectTimeout = timeout;
        EngineFactory factory = getEngineFactory();
        if (factory != null) {
            factory.getEngine().setConnectTimeout(timeout);
        }
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setSoTimeout(int timeout) {
        this.soTimeout = timeout;
        EngineFactory factory = getEngineFactory();
        if (factory != null) {
            factory.getEngine().setSoTimeout(timeout);
        }
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    private void initExecutor(int maxThread) {
        this.executor = createExecutor(maxThread);
        setSemaphore(maxThread);
        setConnectTimeout(30000);
        setSoTimeout(30000);
    }

    public void setSSLVerifier(SSLVerifier verifier) {
        SSLVerifier sslVerifier = verifier;
        if (sslVerifier == null) {
            sslVerifier = SSLUtils.getVerifier(null);
        }
        if (socketFactory == null) {
            socketFactory = new DefaultSocketFactory(sslVerifier);
        }
        getEngineFactory().getEngine().setSSLSocketFactory(socketFactory);
    }

    public synchronized void setSemaphore(int permits) {
        if (semaphore == null) {
            semaphore = new Semaphore(permits);
        } else {
            int count = permits - this.permits;
            if (count > 0) {
                semaphore.release(permits);
            } else {
                semaphore.acquireUninterruptibly(-count);
            }
        }
        this.permits = permits;
        if (getEngineFactory() != null && getEngineFactory().getEngine() != null) {
            getEngineFactory().getEngine().setMaxConnections(permits);
        }
        if (this.executor != null) {
            this.executor.setCorePoolSize(permits);
            this.executor.setMaximumPoolSize(permits);
        }
    }

    public <T> void request(TransportWorker<T> worker,
                            OnCompleteListener<T> listener, int priority) {
        executor.execute(createRequestTask(worker, listener, priority));
    }

    public <T> void request(TransportWorker<T> worker,
                            OnCompleteListener<T> listener) {
        executor.execute(createRequestTask(worker, listener, 10));
    }

    private <T> Runnable createRequestTask(final TransportWorker<T> worker,
                                           final OnCompleteListener<T> listener, int priority) {
        return new PriorityRunnable(priority) {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(getPriority());
                TransportResult<T> result = requestSync(worker);
                listener.onComplete(TransportManager.this, result);
            }
        };
    }

    public <T> TransportResult<T> requestSync(TransportWorker<T> worker) {
        TransportResult<T> result = null;
        synchronized (worker.getStatusLock()) {
            if (worker.isTransportFinish()) {
                acquireSemaphore();

                worker.cancel();
                worker.addWorkerListener(new TransportWorker.Callback<T>() {
                    @Override
                    public void onWorkerFinish(TransportWorker<T> worker) {
                        getEngineFactory().cancel(worker.getRequest());
                        worker.removeWorkerListener(this);
                    }
                });
                TransportResponse response = getEngineFactory().request(worker.getRequest());
                T t = worker.parseResultData(response);
                result = new DefaultTransportResult<T>(t, new Date(), response);

                releasesSemaphore();
            }
        }
        return result;
    }

    private void acquireSemaphore() {
        this.semaphore.acquireUninterruptibly();
    }

    private void releasesSemaphore() {
        this.semaphore.release();
    }

    protected ThreadPoolExecutor createExecutor(int maxThread) {
        PriorityBlockingQueue pbq = new PriorityBlockingQueue();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                maxThread, maxThread, 3L, TimeUnit.SECONDS, pbq);
        executor.setThreadFactory(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(new PriorityRunnableWrapper(r));
            }
        });
        return executor;
    }

    private static class PriorityRunnableWrapper implements Runnable {
        private Runnable r;

        public PriorityRunnableWrapper(Runnable r) {
            this.r = r;
        }

        @Override
        public void run() {
            android.os.Process.setThreadPriority(10);
            r.run();
        }
    }
}
