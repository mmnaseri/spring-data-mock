package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.store.DataStoreEvent;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListener;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListenerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>This is the default implementation for the {@link DataStoreEventListenerContext} interface.
 * This implementation comes with support for parent context lookup, meaning that you can register event listeners with
 * another context, set that as the parent for this context, and have appropriate events triggered in that context as
 * well.</p>
 *
 * <p>It should be noted that listeners registered in the current context always take precedence over the
 * listeners found on a possible parent context.</p>
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/12/15)
 */
public class DefaultDataStoreEventListenerContext implements DataStoreEventListenerContext {

    private static final Log log = LogFactory.getLog(DefaultDataStoreEventListenerContext.class);
    private final ConcurrentMap<Class<? extends DataStoreEvent>, List<DataStoreEventListener<?>>> listeners;
    private final DataStoreEventListenerContext parent;

    public DefaultDataStoreEventListenerContext() {
        this(null);
    }

    public DefaultDataStoreEventListenerContext(DataStoreEventListenerContext parent) {
        this.parent = parent;
        listeners = new ConcurrentHashMap<>();
    }

    @Override
    public <E extends DataStoreEvent> void register(DataStoreEventListener<E> listener) {
        final SmartDataStoreEventListener<E> eventListener = new SmartDataStoreEventListener<>(listener);
        listeners.putIfAbsent(eventListener.getEventType(), new CopyOnWriteArrayList<DataStoreEventListener<?>>());
        log.info("Registering an event listener for type " + eventListener.getEventType());
        listeners.get(eventListener.getEventType()).add(eventListener);
    }

    @Override
    public void trigger(DataStoreEvent event) {
        if (event == null) {
            log.error("The data store event that was triggered was a null value");
            throw new InvalidArgumentException("Cannot raise a null event");
        }
        log.info("Triggering data store event of type " + event.getClass());
        for (Class<? extends DataStoreEvent> eventType : listeners.keySet()) {
            if (eventType.isInstance(event)) {
                for (DataStoreEventListener listener : listeners.get(eventType)) {
                    log.debug("Triggering event on listener " + ((SmartDataStoreEventListener) listener).getDelegate());
                    //noinspection unchecked
                    listener.onEvent(event);
                }
            }
        }
        if (parent != null) {
            log.info("Going to trigger the same event on the parent context");
            parent.trigger(event);
        }
    }

    @Override
    public <E extends DataStoreEvent> List<DataStoreEventListener<? extends E>> getListeners(Class<E> eventType) {
        final List<DataStoreEventListener<? extends E>> found = new LinkedList<>();
        for (Class<? extends DataStoreEvent> supportedType : listeners.keySet()) {
            if (supportedType.isAssignableFrom(eventType)) {
                for (DataStoreEventListener listener : listeners.get(supportedType)) {
                    //noinspection unchecked
                    found.add(((SmartDataStoreEventListener) listener).getDelegate());
                }
            }
        }
        if (parent != null) {
            found.addAll(parent.getListeners(eventType));
        }
        return found;
    }

}
