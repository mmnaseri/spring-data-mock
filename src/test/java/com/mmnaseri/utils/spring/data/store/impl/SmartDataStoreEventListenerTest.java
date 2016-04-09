package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.store.DataStoreEventListener;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/9/16)
 */
public class SmartDataStoreEventListenerTest {

    private static class SpyingEventListener implements DataStoreEventListener<AbstractDataStoreEvent> {

        private final List<AbstractDataStoreEvent> events = new ArrayList<>();

        @Override
        public void onEvent(AbstractDataStoreEvent event) {
            events.add(event);
        }

        public List<AbstractDataStoreEvent> getEvents() {
            return events;
        }

    }

    @Test
    public void testEventTypeRecognition() throws Exception {
        final SmartDataStoreEventListener<AbstractDataStoreEvent> listener = new SmartDataStoreEventListener<>(new SpyingEventListener());
        assertThat(listener.getEventType(), is(notNullValue()));
        assertThat(listener.getEventType(), is(equalTo(AbstractDataStoreEvent.class)));
    }

    @Test
    public void testEventDelegation() throws Exception {
        final SpyingEventListener spy = new SpyingEventListener();
        final SmartDataStoreEventListener<AbstractDataStoreEvent> listener = new SmartDataStoreEventListener<>(spy);
        final AfterDeleteDataStoreEvent first = new AfterDeleteDataStoreEvent(null, null, null);
        final BeforeInsertDataStoreEvent second = new BeforeInsertDataStoreEvent(null, null, null);
        listener.onEvent(first);
        listener.onEvent(second);
        assertThat(spy.getEvents(), hasSize(2));
        assertThat(spy.getEvents().get(0), Matchers.<AbstractDataStoreEvent>is(first));
        assertThat(spy.getEvents().get(1), Matchers.<AbstractDataStoreEvent>is(second));
    }
    
}