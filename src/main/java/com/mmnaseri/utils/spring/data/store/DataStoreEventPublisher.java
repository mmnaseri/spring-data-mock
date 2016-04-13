package com.mmnaseri.utils.spring.data.store;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/6/15)
 */
public interface DataStoreEventPublisher {

    void publishEvent(DataStoreEvent event);

}
