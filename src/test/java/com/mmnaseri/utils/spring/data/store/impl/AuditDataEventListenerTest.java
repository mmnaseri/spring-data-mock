package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableRepositoryMetadata;
import com.mmnaseri.utils.spring.data.sample.models.AuditableEntity;
import com.mmnaseri.utils.spring.data.sample.models.ImplicitlyAuditableEntity;
import com.mmnaseri.utils.spring.data.sample.usecases.store.SampleAuditorAware;
import org.testng.annotations.Test;

import java.util.Date;

import static com.mmnaseri.utils.spring.data.sample.usecases.store.SampleAuditorAware.AUDITOR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/9/16)
 */
public class AuditDataEventListenerTest {

    @Test
    public void testBeforeInsertForImplicitAuditing() throws Exception {
        final AuditDataEventListener listener = new AuditDataEventListener(new SampleAuditorAware());
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, ImplicitlyAuditableEntity.class, null, "id");
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
    public void testBeforeUpdateForImplicitAuditing() throws Exception {
        final AuditDataEventListener listener = new AuditDataEventListener(new SampleAuditorAware());
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, ImplicitlyAuditableEntity.class, null, "id");
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
    public void testBeforeInsertForExplicitAuditing() throws Exception {
        final AuditDataEventListener listener = new AuditDataEventListener(new SampleAuditorAware());
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, AuditableEntity.class, null, "id");
        final AuditableEntity entity = new AuditableEntity();
        final Date before = new Date();
        listener.onEvent(new BeforeInsertDataStoreEvent(repositoryMetadata, null, entity));
        final Date after = new Date();
        assertThat(entity.getCreatedBy(), is(notNullValue()));
        assertThat(entity.getCreatedBy(), is(AUDITOR));
        assertThat(entity.getCreatedDate(), is(notNullValue()));
        assertThat(entity.getCreatedDate().toDate().getTime(), is(greaterThanOrEqualTo(before.getTime())));
        assertThat(entity.getCreatedDate().toDate().getTime(), is(lessThanOrEqualTo(after.getTime())));
        assertThat(entity.getLastModifiedDate(), is(nullValue()));
        assertThat(entity.getLastModifiedBy(), is(nullValue()));
    }

    @Test
    public void testBeforeUpdateForExplicitAuditing() throws Exception {
        final AuditDataEventListener listener = new AuditDataEventListener(new SampleAuditorAware());
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, AuditableEntity.class, null, "id");
        final AuditableEntity entity = new AuditableEntity();
        final Date before = new Date();
        listener.onEvent(new BeforeUpdateDataStoreEvent(repositoryMetadata, null, entity));
        final Date after = new Date();
        assertThat(entity.getLastModifiedBy(), is(notNullValue()));
        assertThat(entity.getLastModifiedBy(), is(AUDITOR));
        assertThat(entity.getLastModifiedDate(), is(notNullValue()));
        assertThat(entity.getLastModifiedDate().toDate().getTime(), is(greaterThanOrEqualTo(before.getTime())));
        assertThat(entity.getLastModifiedDate().toDate().getTime(), is(lessThanOrEqualTo(after.getTime())));
        assertThat(entity.getCreatedDate(), is(nullValue()));
        assertThat(entity.getCreatedBy(), is(nullValue()));
    }

}