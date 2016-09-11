package com.esummer.android.transport;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by cwj on 16/7/15.
 */
public abstract class EngineFactory {
    private TransportEngine engine;
    // 可以考虑ConcurrentHashMap替换
    private HashSet<TransportRequest> requestSet;

    public EngineFactory() {
        engine = createTransportEngine(5);
        newRequestSet();
    }

    public EngineFactory(int maxConnection) {
        engine = createTransportEngine(maxConnection);
        engine.setMaxConnections(maxConnection);
        newRequestSet();
    }
    protected abstract TransportEngine createTransportEngine(int maxTotalConnections);

    private void newRequestSet() {
        requestSet = new HashSet<>();
    }

    private void addRequest(TransportRequest request) {
        synchronized (requestSet) {
            requestSet.add(request);
        }
    }

    private boolean removeRequest(TransportRequest request) {
        synchronized (requestSet) {
            return requestSet.remove(request);
        }
    }

    public TransportEngine getEngine() {
        return engine;
    }

    public TransportResponse request(TransportRequest request) {
        addRequest(request);
        try {
            TransportResponse response = getEngine().getTransport().execute(request);
            removeRequest(request);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            removeRequest(request);
        }
        return null;
    }

    public void cancel(final TransportRequest request) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                request.abort();
                removeRequest(request);
            }
        }).start();
    }
}
