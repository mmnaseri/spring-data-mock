package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.store.DataStoreEvent;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListener;
import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;
import org.springframework.data.domain.AuditorAware;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/12/15)
 */
public class AuditDataEventListener implements DataStoreEventListener<DataStoreEvent> {

    private final AuditorAware auditorAware;

    public AuditDataEventListener(AuditorAware auditorAware) {
        this.auditorAware = auditorAware;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onEvent(DataStoreEvent event) {
        if (event instanceof BeforeInsertDataStoreEvent) {
            final Object entity = ((BeforeInsertDataStoreEvent) event).getEntity();
            final Auditable wrapper = getAuditable(entity, event.getRepositoryMetadata());
            wrapper.setCreatedBy(auditorAware == null ? null : auditorAware.getCurrentAuditor());
            wrapper.setCreatedDate(DateTime.now());
        } else if (event instanceof BeforeUpdateDataStoreEvent) {
            final Object entity = ((BeforeUpdateDataStoreEvent) event).getEntity();
            final Auditable wrapper = getAuditable(entity, event.getRepositoryMetadata());
            wrapper.setLastModifiedBy(auditorAware == null ? null : auditorAware.getCurrentAuditor());
            wrapper.setLastModifiedDate(DateTime.now());
        }
    }
    
    private static Auditable getAuditable(Object entity, RepositoryMetadata repositoryMetadata) {
        if (entity instanceof Auditable) {
            return (Auditable) entity;
        } else {
            return new AuditableWrapper(entity, repositoryMetadata);
        }
    }

    public AuditorAware getAuditorAware() {
        return auditorAware;
    }

}
