package com.esummer.android.transport;

/**
 * Created by cwj on 16/7/15.
 */
public interface TransportResult<ResultType> {
    /**
     * Returns the response data.
     *
     * @return maybe return null, if {@link TransportResult#isSuccess()} is not true.
     */
    ResultType getResultData();

    /**
     * Called when response completed
     * @param Complete
     */
    void setComplete(boolean Complete);

    /**
     * Returns true if {@link TransportResult#getResultData()} can get data, otherwise false.
     * @return
     */
    boolean isSuccess();
}
