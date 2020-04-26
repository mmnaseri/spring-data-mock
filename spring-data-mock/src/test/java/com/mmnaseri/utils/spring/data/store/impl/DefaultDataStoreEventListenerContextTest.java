package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.sample.mocks.AfterInsertEventListener;
import com.mmnaseri.utils.spring.data.sample.mocks.AllCatchingEventListener;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListener;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/9/16)
 */
public class DefaultDataStoreEventListenerContextTest {

    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testTriggeringNullEvent() {
        DefaultDataStoreEventListenerContext context = new DefaultDataStoreEventListenerContext();
        context.trigger(null);
    }

    @Test
    public void testExactEventType() {
        final AfterInsertEventListener first = new AfterInsertEventListener();
        final AfterInsertEventListener second = new AfterInsertEventListener();
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
    public void testParentEventType() {
        final AllCatchingEventListener first = new AllCatchingEventListener();
        final AfterInsertEventListener second = new AfterInsertEventListener();
        final AfterInsertDataStoreEvent event = new AfterInsertDataStoreEvent(null, null, null);
        final DefaultDataStoreEventListenerContext context = new DefaultDataStoreEventListenerContext();
        context.register(first);
        context.register(second);
        context.trigger(event);
        assertThat(first.getEvents(), hasSize(1));
        assertThat(first.getEvents().get(0), Matchers.is(event));
        assertThat(second.getEvents(), hasSize(1));
        assertThat(second.getEvents().get(0), is(event));
    }

    @Test
    public void testEventPropagation() {
        final AfterInsertDataStoreEvent firstEvent = new AfterInsertDataStoreEvent(null, null, null);
        final AfterInsertDataStoreEvent secondEvent = new AfterInsertDataStoreEvent(null, null, null);
        final AfterInsertEventListener first = new AfterInsertEventListener();
        final AfterInsertEventListener second = new AfterInsertEventListener();
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

    @Test
    public void testFindingEventListeners() {
        final AfterInsertEventListener first = new AfterInsertEventListener();
        final AfterInsertEventListener second = new AfterInsertEventListener();
        final DefaultDataStoreEventListenerContext parent = new DefaultDataStoreEventListenerContext();
        final DefaultDataStoreEventListenerContext child = new DefaultDataStoreEventListenerContext(parent);
        parent.register(first);
        child.register(second);
        assertThat(parent.getListeners(AfterInsertDataStoreEvent.class), hasSize(1));
        assertThat(parent.getListeners(AfterInsertDataStoreEvent.class).get(0),
                   Matchers.<DataStoreEventListener>is(first));
        assertThat(child.getListeners(AfterInsertDataStoreEvent.class), hasSize(2));
        assertThat(child.getListeners(AfterInsertDataStoreEvent.class).get(0),
                   Matchers.<DataStoreEventListener>is(second));
        assertThat(child.getListeners(AfterInsertDataStoreEvent.class).get(1),
                   Matchers.<DataStoreEventListener>is(first));
    }

}