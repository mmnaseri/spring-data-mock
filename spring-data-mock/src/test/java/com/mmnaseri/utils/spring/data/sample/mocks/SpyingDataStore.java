package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.QueueingDataStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public class SpyingDataStore<K, E> implements QueueingDataStore<K, E, Object> {

    private final DataStore<K, E> delegate;
    private final List<OperationRequest> requests;
    private final AtomicLong counter;
    private final Stack<Object> batches = new Stack<>();

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

    @Override
    public void truncate() {
        if (delegate != null) {
            delegate.truncate();
        }
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

    @Override
    public Object startBatch() {
        final Object batch = new Object();
        batches.add(batch);
        return batch;
    }

    @Override
    public void endBatch(Object batch) {
        assertThat(batches, is(not(empty())));
        final Object lastBatch = batches.pop();
        assertThat(batch, is(lastBatch));
    }

}
