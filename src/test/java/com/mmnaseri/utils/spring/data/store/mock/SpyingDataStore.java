package com.mmnaseri.utils.spring.data.store.mock;

import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.QueueingDataStore;

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
public class SpyingDataStore<K extends Serializable, E> implements QueueingDataStore<K, E> {

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
        requests.add(new OperationRequest<>(counter.incrementAndGet(), Operation.CHECK, key, null));
        return delegate.hasKey(key);
    }

    @Override
    public boolean save(K key, E entity) {
        requests.add(new OperationRequest<>(counter.incrementAndGet(), Operation.SAVE, key, entity));
        return delegate != null && delegate.save(key, entity);
    }

    @Override
    public boolean delete(K key) {
        requests.add(new OperationRequest<>(counter.incrementAndGet(), Operation.DELETE, key, null));
        return delegate != null && delegate.delete(key);
    }

    @Override
    public E retrieve(K key) {
        requests.add(new OperationRequest<>(counter.incrementAndGet(), Operation.READ, key, null));
        if (delegate == null) {
            return null;
        }
        return delegate.retrieve(key);
    }

    @Override
    public Collection<K> keys() {
        requests.add(new OperationRequest<>(counter.incrementAndGet(), Operation.CHECK, null, null));
        if (delegate == null) {
            return Collections.emptySet();
        }
        return delegate.keys();
    }

    @Override
    public Collection<E> retrieveAll() {
        requests.add(new OperationRequest<>(counter.incrementAndGet(), Operation.READ, null, null));
        if (delegate == null) {
            return Collections.emptySet();
        }
        return delegate.retrieveAll();
    }

    @Override
    public Class<E> getEntityType() {
        if (delegate == null) {
            return null;
        }
        return delegate.getEntityType();
    }

    public List<OperationRequest> getRequests() {
        return Collections.unmodifiableList(requests);
    }

    public void reset() {
        requests.clear();
    }

    public DataStore<K, E> getDelegate() {
        return delegate;
    }

    @Override
    public void flush() {
        requests.add(new OperationRequest<>(counter.incrementAndGet(), Operation.FLUSH, null, null));
    }

}
