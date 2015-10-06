package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.store.DataStoreEvent;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListener;
import org.springframework.core.GenericTypeResolver;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public class SmartDataStoreEventListener<E extends DataStoreEvent> implements DataStoreEventListener<E> {

    private final DataStoreEventListener<E> delegate;
    private final Class<E> eventType;

    public SmartDataStoreEventListener(DataStoreEventListener<E> delegate) {
        this.delegate = delegate;
        //noinspection unchecked
        eventType = (Class<E>) GenericTypeResolver.resolveTypeArgument(delegate.getClass(), DataStoreEventListener.class);
    }

    @Override
    public void onEvent(E event) {
        delegate.onEvent(event);
    }

    public Class<E> getEventType() {
        return eventType;
    }

}
