package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.store.DataStoreEvent;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListener;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListenerContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public class SpyingListenerContext implements DataStoreEventListenerContext {

    private final List<EventTrigger> events = new ArrayList<>();
    private final AtomicLong counter;

    public SpyingListenerContext(AtomicLong counter) {
        this.counter = counter;
    }

    @Override
    public <E extends DataStoreEvent> void register(DataStoreEventListener<E> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void trigger(DataStoreEvent event) {
        events.add(new EventTrigger(counter.incrementAndGet(), event));
    }

    @Override
    public <E extends DataStoreEvent> List<DataStoreEventListener<? extends E>> getListeners(Class<E> eventType) {
        throw new UnsupportedOperationException();
    }

    public List<EventTrigger> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public void reset() {
        events.clear();
    }

}
