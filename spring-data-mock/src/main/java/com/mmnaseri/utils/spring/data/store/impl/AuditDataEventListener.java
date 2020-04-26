package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.store.DataStoreEvent;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListener;
import org.springframework.data.domain.Auditable;
import org.springframework.data.domain.AuditorAware;

import java.time.Instant;

/**
 * This event listener can be registered with an
 * {@link com.mmnaseri.utils.spring.data.store.DataStoreEventListenerContext
 * event listener context} to add support for auditing entities as per the specifications set forth by Spring Data
 * Commons.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
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
            wrapper.setCreatedBy(auditorAware == null ? null : auditorAware.getCurrentAuditor().orElse(null));
            wrapper.setCreatedDate(Instant.now());
        } else if (event instanceof BeforeUpdateDataStoreEvent) {
            final Object entity = ((BeforeUpdateDataStoreEvent) event).getEntity();
            final Auditable wrapper = getAuditable(entity, event.getRepositoryMetadata());
            wrapper.setLastModifiedBy(auditorAware == null ? null : auditorAware.getCurrentAuditor().orElse(null));
            wrapper.setLastModifiedDate(Instant.now());
        }
    }

    /**
     * Given an entity returns an {@link Auditable} for it. If the entity itself implements that interface, it will be
     * returned without any changes, otherwise it will be wrapped in an {@link AuditableWrapper}.
     *
     * @param entity             the entity
     * @param repositoryMetadata the repository metadata for the entity
     * @return the auditable entity
     */
    private static Auditable getAuditable(Object entity, RepositoryMetadata repositoryMetadata) {
        if (entity instanceof Auditable) {
            return (Auditable) entity;
        } else {
            return new AuditableWrapper(entity, repositoryMetadata);
        }
    }

    /**
     * @return the auditor aware that is being used by this listener for setting auditor related properties
     */
    public AuditorAware getAuditorAware() {
        return auditorAware;
    }

}
