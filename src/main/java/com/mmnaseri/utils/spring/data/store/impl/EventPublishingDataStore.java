package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.error.CorruptDataException;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreEvent;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.DataStoreEventPublisher;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public class EventPublishingDataStore<K extends Serializable, E> implements DataStore<K, E>, DataStoreEventPublisher {

    private final DataStore<K, E> delegate;
    private final RepositoryMetadata repositoryMetadata;
    private final DataStoreEventListenerContext listenerContext;

    public EventPublishingDataStore(DataStore<K, E> delegate, RepositoryMetadata repositoryMetadata, DataStoreEventListenerContext listenerContext) {
        this.delegate = delegate;
        this.repositoryMetadata = repositoryMetadata;
        this.listenerContext = listenerContext;
    }

    @Override
    public boolean hasKey(K key) {
        return delegate.hasKey(key);
    }

    @Override
    public void save(K key, E entity) {
        if (key == null) {
            throw new CorruptDataException(getEntityType(), null, "Cannot save an entity with a null key");
        }
        if (entity == null) {
            throw new CorruptDataException(getEntityType(), null, "Cannot save null into the data store");
        }
        final boolean entityIsNew = !delegate.hasKey(key);
        if (entityIsNew) {
            publishEvent(new BeforeInsertDataStoreEvent(repositoryMetadata, this, entity));
        } else {
            publishEvent(new BeforeUpdateDataStoreEvent(repositoryMetadata, this, entity));
        }
        delegate.save(key, entity);
        if (entityIsNew) {
            publishEvent(new AfterInsertDataStoreEvent(repositoryMetadata, this, entity));
        } else {
            publishEvent(new AfterUpdateDataStoreEvent(repositoryMetadata, this, entity));
        }
    }

    @Override
    public void delete(K key) {
        if (!delegate.hasKey(key)) {
            return;
        }
        final E entity = delegate.retrieve(key);
        publishEvent(new BeforeDeleteDataStoreEvent(repositoryMetadata, this, entity));
        delegate.delete(key);
        publishEvent(new AfterDeleteDataStoreEvent(repositoryMetadata, this, entity));
    }

    @Override
    public E retrieve(K key) {
        return delegate.retrieve(key);
    }

    @Override
    public Collection<E> retrieveAll() {
        return delegate.retrieveAll();
    }

    @Override
    public Class<E> getEntityType() {
        return delegate.getEntityType();
    }

    @Override
    public void publishEvent(DataStoreEvent event) {
         listenerContext.trigger(event);
    }

}
