package com.mmnaseri.utils.spring.data.store.mock;

import com.mmnaseri.utils.spring.data.store.DataStoreEvent;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class EventTrigger {

    private final Long timestamp;
    private final DataStoreEvent event;

    public EventTrigger(Long timestamp, DataStoreEvent event) {
        this.timestamp = timestamp;
        this.event = event;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public DataStoreEvent getEvent() {
        return event;
    }
}
