package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.store.DataStoreEvent;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListener;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListenerContext;

/**
 * Lets us define the various listener configurations used when data operations are taking place
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
@SuppressWarnings("WeakerAccess")
public interface EventListener extends Auditing {

    /**
     * Tells the build to use this listener context
     *
     * @param context the context
     * @return the rest of the configuration
     */
    End withListeners(DataStoreEventListenerContext context);

    /**
     * Tells the context to register this listener
     *
     * @param listener the listener
     * @param <E>      the (super-)type of the events the listener is going to react to
     * @return the rest of the configuration
     */
    <E extends DataStoreEvent> EventListenerAnd withListener(DataStoreEventListener<E> listener);

}
