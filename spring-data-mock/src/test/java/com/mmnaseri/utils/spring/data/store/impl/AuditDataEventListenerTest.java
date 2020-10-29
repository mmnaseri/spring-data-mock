package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableRepositoryMetadata;
import com.mmnaseri.utils.spring.data.sample.models.AuditableEntity;
import com.mmnaseri.utils.spring.data.sample.models.ImplicitlyAuditableEntity;
import com.mmnaseri.utils.spring.data.sample.usecases.store.SampleAuditorAware;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Optional;

import static com.mmnaseri.utils.spring.data.sample.usecases.store.SampleAuditorAware.AUDITOR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/9/16)
 */
public class AuditDataEventListenerTest {

    @Test
    public void testBeforeInsertForImplicitAuditing() {
        final AuditDataEventListener listener = new AuditDataEventListener(new SampleAuditorAware());
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class,
                                                                                      ImplicitlyAuditableEntity.class,
                                                                                      null, "id");
        final ImplicitlyAuditableEntity entity = new ImplicitlyAuditableEntity();
        final Date before = new Date();
        listener.onEvent(new BeforeInsertDataStoreEvent(repositoryMetadata, null, entity));
        final Date after = new Date();
        assertThat(entity.getCreatedBy(), is(notNullValue()));
        assertThat(entity.getCreatedBy(), is(AUDITOR));
        assertThat(entity.getCreatedDate(), is(notNullValue()));
        assertThat(entity.getCreatedDate().getTime(), is(greaterThanOrEqualTo(before.getTime())));
        assertThat(entity.getCreatedDate().getTime(), is(lessThanOrEqualTo(after.getTime())));
        assertThat(entity.getLastModifiedDate(), is(nullValue()));
        assertThat(entity.getLastModifiedBy(), is(nullValue()));
    }

    @Test
    public void testBeforeUpdateForImplicitAuditing() {
        final AuditDataEventListener listener = new AuditDataEventListener(new SampleAuditorAware());
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class,
                                                                                      ImplicitlyAuditableEntity.class,
                                                                                      null, "id");
        final ImplicitlyAuditableEntity entity = new ImplicitlyAuditableEntity();
        final Date before = new Date();
        listener.onEvent(new BeforeUpdateDataStoreEvent(repositoryMetadata, null, entity));
        final Date after = new Date();
        assertThat(entity.getLastModifiedBy(), is(notNullValue()));
        assertThat(entity.getLastModifiedBy(), is(AUDITOR));
        assertThat(entity.getLastModifiedDate(), is(notNullValue()));
        assertThat(entity.getLastModifiedDate().getTime(), is(greaterThanOrEqualTo(before.getTime())));
        assertThat(entity.getLastModifiedDate().getTime(), is(lessThanOrEqualTo(after.getTime())));
        assertThat(entity.getCreatedDate(), is(nullValue()));
        assertThat(entity.getCreatedBy(), is(nullValue()));
    }

    @Test
    public void testBeforeInsertForExplicitAuditing() {
        final AuditDataEventListener listener = new AuditDataEventListener(new SampleAuditorAware());
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class,
                                                                                      AuditableEntity.class, null,
                                                                                      "id");
        final AuditableEntity entity = new AuditableEntity();
        final Date before = new Date();
        listener.onEvent(new BeforeInsertDataStoreEvent(repositoryMetadata, null, entity));
        final Date after = new Date();
        assertThat(entity.getCreatedBy().isPresent(), is(true));
        assertThat(entity.getCreatedBy().get(), is(notNullValue()));
        assertThat(entity.getCreatedBy().get(), is(AUDITOR));
        assertThat(entity.getCreatedDate().isPresent(), is(true));
        assertThat(entity.getCreatedDate().get(), is(notNullValue()));
        assertThat(entity.getCreatedDate().get().toEpochMilli(), is(greaterThanOrEqualTo(before.getTime())));
        assertThat(entity.getCreatedDate().get().toEpochMilli(), is(lessThanOrEqualTo(after.getTime())));
        assertThat(entity.getLastModifiedDate(), is(Optional.empty()));
        assertThat(entity.getLastModifiedBy(), is(Optional.empty()));
    }

    @Test
    public void testBeforeUpdateForExplicitAuditing() {
        final AuditDataEventListener listener = new AuditDataEventListener(new SampleAuditorAware());
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class,
                                                                                      AuditableEntity.class, null,
                                                                                      "id");
        final AuditableEntity entity = new AuditableEntity();
        final Date before = new Date();
        listener.onEvent(new BeforeUpdateDataStoreEvent(repositoryMetadata, null, entity));
        final Date after = new Date();
        assertThat(entity.getLastModifiedBy().isPresent(), is(true));
        assertThat(entity.getLastModifiedBy().get(), is(notNullValue()));
        assertThat(entity.getLastModifiedBy().get(), is(AUDITOR));
        assertThat(entity.getLastModifiedDate().isPresent(), is(true));
        assertThat(entity.getLastModifiedDate().get(), is(notNullValue()));
        assertThat(entity.getLastModifiedDate().get().toEpochMilli(), is(greaterThanOrEqualTo(before.getTime())));
        assertThat(entity.getLastModifiedDate().get().toEpochMilli(), is(lessThanOrEqualTo(after.getTime())));
        assertThat(entity.getCreatedDate(), is(Optional.empty()));
        assertThat(entity.getCreatedBy(), is(Optional.empty()));
    }

}