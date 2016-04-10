package com.mmnaseri.utils.spring.data.store.mock;

import com.mmnaseri.utils.spring.data.store.DataStore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class SpyingDataStore<K extends Serializable, E> implements DataStore<K, E> {

    private final DataStore<K, E> delegate;
    private final List<OperationRequest> requests;
    private final AtomicLong counter;

    public SpyingDataStore(DataStore<K, E> delegate, AtomicLong counter) {
        this.delegate = delegate;
        this.counter = counter;
        requests = new ArrayList<>();
    }

    @Override
    public boolean hasKey(K key) {
        return delegate.hasKey(key);
    }

    @Override
    public boolean save(K key, E entity) {
        requests.add(new OperationRequest<>(counter.incrementAndGet(), Operation.SAVE, key, entity));
        return delegate.save(key, entity);
    }

    @Override
    public boolean delete(K key) {
        requests.add(new OperationRequest<>(counter.incrementAndGet(), Operation.DELETE, key, null));
        return delegate.delete(key);
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

    public List<OperationRequest> getRequests() {
        return Collections.unmodifiableList(requests);
    }

    public void reset() {
        requests.clear();
    }

}
