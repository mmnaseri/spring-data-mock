package com.mmnaseri.utils.spring.data.dsl.config;

import com.mmnaseri.utils.spring.data.store.DataStoreEventListenerContext;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/12/15)
 */
public interface EventListenerContextConfigurer extends QueryExtractionConfigurer {

    QueryExtractionConfigurer withEventListeners(DataStoreEventListenerContext listenerContext);

}
