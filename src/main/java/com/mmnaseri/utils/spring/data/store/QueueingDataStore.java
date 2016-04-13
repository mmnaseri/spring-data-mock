package com.mmnaseri.utils.spring.data.store;

import java.io.Serializable;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/11/16, 1:18 PM)
 */
public interface QueueingDataStore<K extends Serializable, E, B> extends DataStore<K, E> {

    void flush();

    B startBatch();

    void endBatch(B batch);

}
