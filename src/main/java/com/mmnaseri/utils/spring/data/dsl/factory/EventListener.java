package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.store.DataStoreEvent;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListener;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListenerContext;
import org.springframework.data.domain.AuditorAware;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
public interface EventListener extends End {

    End withListeners(DataStoreEventListenerContext context);

    <E extends DataStoreEvent> EventListenerAnd withListener(DataStoreEventListener<E> listener);

    EventListener enableAuditing(AuditorAware auditorAware);

    EventListener enableAuditing();

}
