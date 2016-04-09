package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.store.DataStoreEvent;
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
public class DefaultDataStoreEventListenerContextTest {

    private static class SpyingEventListener<E extends DataStoreEvent> implements DataStoreEventListener<E> {

        private final List<E> events = new ArrayList<>();

        @Override
        public void onEvent(E event) {
            events.add(event);
        }

        public List<E> getEvents() {
            return events;
        }

    }

    private static class AllCatchingEventListener extends SpyingEventListener<DataStoreEvent> {}

    private static class InsertEventListener extends SpyingEventListener<AfterInsertDataStoreEvent> {}

    @Test
    public void testExactEventType() throws Exception {
        final InsertEventListener first = new InsertEventListener();
        final InsertEventListener second = new InsertEventListener();
        final AfterInsertDataStoreEvent event = new AfterInsertDataStoreEvent(null, null, null);
        final DefaultDataStoreEventListenerContext context = new DefaultDataStoreEventListenerContext();
        context.register(first);
        context.register(second);
        context.trigger(event);
        assertThat(first.getEvents(), hasSize(1));
        assertThat(first.getEvents().get(0), is(event));
        assertThat(second.getEvents(), hasSize(1));
        assertThat(second.getEvents().get(0), is(event));
    }

    @Test
    public void testParentEventType() throws Exception {
        final AllCatchingEventListener first = new AllCatchingEventListener();
        final InsertEventListener second = new InsertEventListener();
        final AfterInsertDataStoreEvent event = new AfterInsertDataStoreEvent(null, null, null);
        final DefaultDataStoreEventListenerContext context = new DefaultDataStoreEventListenerContext();
        context.register(first);
        context.register(second);
        context.trigger(event);
        assertThat(first.getEvents(), hasSize(1));
        assertThat(first.getEvents().get(0), Matchers.<DataStoreEvent>is(event));
        assertThat(second.getEvents(), hasSize(1));
        assertThat(second.getEvents().get(0), is(event));
    }

    @Test
    public void testEventPropagation() throws Exception {
        final AfterInsertDataStoreEvent firstEvent = new AfterInsertDataStoreEvent(null, null, null);
        final AfterInsertDataStoreEvent secondEvent = new AfterInsertDataStoreEvent(null, null, null);
        final InsertEventListener first = new InsertEventListener();
        final InsertEventListener second = new InsertEventListener();
        final DefaultDataStoreEventListenerContext parent = new DefaultDataStoreEventListenerContext();
        final DefaultDataStoreEventListenerContext child = new DefaultDataStoreEventListenerContext(parent);
        parent.register(first);
        child.register(second);
        child.trigger(firstEvent);
        parent.trigger(secondEvent);
        assertThat(first.getEvents(), hasSize(2));
        assertThat(first.getEvents(), contains(firstEvent, secondEvent));
        assertThat(second.getEvents(), hasSize(1));
        assertThat(second.getEvents().get(0), is(firstEvent));
    }

}