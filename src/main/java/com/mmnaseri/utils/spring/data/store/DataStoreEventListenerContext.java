package com.mmnaseri.utils.spring.data.store;

import java.util.List;

/**
 * This interface is used to manage events related to a data store
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/12/15)
 */
public interface DataStoreEventListenerContext {

    /**
     * Registers a new listener
     * @param listener    the listener
     * @param <E>         the type of events to which the listener wants to subscribe
     */
    <E extends DataStoreEvent> void register(DataStoreEventListener<E> listener);

    /**
     * Triggers an event on the context
     * @param event    the event
     */
    void trigger(DataStoreEvent event);

    /**
     * Returns a list of all event listeners that would be triggered by an event of the given type through
     * this context
     * @param eventType    the event type
     * @param <E>          the event type
     * @return a list of listeners in the order in which they would be called
     */
    <E extends DataStoreEvent> List<DataStoreEventListener<? extends E>> getListeners(Class<E> eventType);

}
