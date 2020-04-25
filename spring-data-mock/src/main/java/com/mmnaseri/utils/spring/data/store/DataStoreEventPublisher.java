package com.mmnaseri.utils.spring.data.store;

/**
 * This interface abstracts the process of publishing a data store event
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public interface DataStoreEventPublisher {

    /**
     * This method is called to publish a data store event
     *
     * @param event the event that should be published
     */
    void publishEvent(DataStoreEvent event);

}
