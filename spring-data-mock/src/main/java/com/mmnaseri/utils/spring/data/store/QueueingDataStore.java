package com.mmnaseri.utils.spring.data.store;

/**
 * This interface indicates that the implementing data store has queueing capabilities and can thus be called upon
 * to flush the queue and commit the results, and more over, handle operations in batches by refraining to flush the
 * queue automatically while a batch is in progress.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/11/16, 1:18 PM)
 */
public interface QueueingDataStore<K, E, B> extends DataStore<K, E> {

    /**
     * Should be called to manually trigger a flush
     */
    void flush();

    /**
     * Starts a batch
     * @return returns a key that can be used to identify this batch and {@link #endBatch(Object) end it}
     */
    B startBatch();

    /**
     * Ends the indicated batch. Note that ending the batch does not necessarily flush the queue if the threshold
     * for the underlying data store has not been reached.
     * @param batch    the batch to end
     */
    void endBatch(B batch);

}
