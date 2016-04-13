package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.store.DataStoreEvent;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListener;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
public interface EventListenerAnd extends End {

    <E extends DataStoreEvent> EventListenerAnd and(DataStoreEventListener<E> listener);

}
