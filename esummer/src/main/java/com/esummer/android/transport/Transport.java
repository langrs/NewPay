package com.esummer.android.transport;

import java.io.IOException;

/**
 * Created by cwj on 16/7/15.
 */
public interface Transport {

    TransportResponse execute(TransportRequest request) throws IOException;

}
