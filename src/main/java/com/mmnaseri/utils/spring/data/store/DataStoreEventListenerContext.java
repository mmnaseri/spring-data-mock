package com.mmnaseri.utils.spring.data.store;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/12/15)
 */
public interface DataStoreEventListenerContext {

    <E extends DataStoreEvent> void register(DataStoreEventListener<E> listener);

    void trigger(DataStoreEvent event);

}
