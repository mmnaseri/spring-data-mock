package com.mmnaseri.utils.spring.data.store.mock;

import com.mmnaseri.utils.spring.data.store.DataStoreEvent;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 1:03 PM)
 */
public class SpyingEventListener<E extends DataStoreEvent> implements DataStoreEventListener<E> {

    private final List<E> events = new ArrayList<>();

    @Override
    public void onEvent(E event) {
        events.add(event);
    }

    public List<E> getEvents() {
        return events;
    }

}
