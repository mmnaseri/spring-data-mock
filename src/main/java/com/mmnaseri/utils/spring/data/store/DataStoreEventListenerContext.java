package com.mmnaseri.utils.spring.data.store;

import java.util.List;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/12/15)
 */
public interface DataStoreEventListenerContext {

    <E extends DataStoreEvent> void register(DataStoreEventListener<E> listener);

    void trigger(DataStoreEvent event);

    <E extends DataStoreEvent> List<DataStoreEventListener<? extends E>> getListeners(Class<E> eventType);
}
