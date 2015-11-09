package com.mmnaseri.utils.spring.data.store;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public interface DataStoreEventListener<E extends DataStoreEvent> {

    void onEvent(E event);

}
