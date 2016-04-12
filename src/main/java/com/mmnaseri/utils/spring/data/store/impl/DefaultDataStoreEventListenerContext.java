package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.store.DataStoreEvent;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListener;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListenerContext;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/12/15)
 */
public class DefaultDataStoreEventListenerContext implements DataStoreEventListenerContext {

    private final ConcurrentMap<Class<? extends DataStoreEvent>, List<DataStoreEventListener<?>>> listeners;
    private final DataStoreEventListenerContext parent;

    public DefaultDataStoreEventListenerContext() {
        this(null);
    }

    public DefaultDataStoreEventListenerContext(DataStoreEventListenerContext parent) {
        this.parent = parent;
        listeners = new ConcurrentHashMap<Class<? extends DataStoreEvent>, List<DataStoreEventListener<?>>>();
    }

    @Override
    public <E extends DataStoreEvent> void register(DataStoreEventListener<E> listener) {
        final SmartDataStoreEventListener<E> eventListener = new SmartDataStoreEventListener<E>(listener);
        listeners.putIfAbsent(eventListener.getEventType(), new CopyOnWriteArrayList<DataStoreEventListener<?>>());
        listeners.get(eventListener.getEventType()).add(eventListener);
    }

    @Override
    public void trigger(DataStoreEvent event) {
        for (Class<? extends DataStoreEvent> eventType : listeners.keySet()) {
            if (eventType.isInstance(event)) {
                for (DataStoreEventListener listener : listeners.get(eventType)) {
                    //noinspection unchecked
                    listener.onEvent(event);
                }
            }
        }
        if (parent != null) {
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
