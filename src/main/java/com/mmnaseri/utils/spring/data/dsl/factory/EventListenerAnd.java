package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.store.DataStoreEvent;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListener;

/**
 * Registers an extra event listener
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
@SuppressWarnings("WeakerAccess")
public interface EventListenerAnd extends Auditing {

    /**
     * Registers an extra event listener
     * @param listener    the listener
     * @param <E>         the type of the events the listener is subscribed to
     * @return the rest of the configuration
     */
    <E extends DataStoreEvent> EventListenerAnd and(DataStoreEventListener<E> listener);

}
