package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableRepositoryMetadata;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.data.domain.AuditorAware;
import org.testng.annotations.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/9/16)
 */
public class AuditDataEventListenerTest {

    private static final String AUDITOR = "AUDITOR";

    private static class ImplicitlyAuditableEntity {

        private String id;
        @CreatedBy
        private String createdBy;
        @LastModifiedBy
        private String lastModifiedBy;
        @CreatedDate
        private Date createdDate;
        @LastModifiedDate
        private Date lastModifiedDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getLastModifiedBy() {
            return lastModifiedBy;
        }

        public void setLastModifiedBy(String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
        }

        public Date getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Date createdDate) {
            this.createdDate = createdDate;
        }

        public Date getLastModifiedDate() {
            return lastModifiedDate;
        }

        public void setLastModifiedDate(Date lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
        }

    }

    private static class AuditableEntity implements Auditable<String, String> {

        private String id;
        private String createdBy;
        private String lastModifiedBy;
        private DateTime createdDate;
        private DateTime lastModifiedDate;

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String getCreatedBy() {
            return createdBy;
        }

        @Override
        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        @Override
        public String getLastModifiedBy() {
            return lastModifiedBy;
        }

        @Override
        public void setLastModifiedBy(String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
        }

        @Override
        public DateTime getCreatedDate() {
            return createdDate;
        }

        @Override
        public void setCreatedDate(DateTime createdDate) {
            this.createdDate = createdDate;
        }

        @Override
        public DateTime getLastModifiedDate() {
            return lastModifiedDate;
        }

        @Override
        public void setLastModifiedDate(DateTime lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public boolean isNew() {
            return getId() == null;
        }

    }

    private static class DefaultAuditorAware implements AuditorAware<String> {

        @Override
        public String getCurrentAuditor() {
            return AUDITOR;
        }

    }

    @Test
    public void testBeforeInsertForImplicitAuditing() throws Exception {
        final AuditDataEventListener listener = new AuditDataEventListener(new DefaultAuditorAware());
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
        final AuditDataEventListener listener = new AuditDataEventListener(new DefaultAuditorAware());
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
        final AuditDataEventListener listener = new AuditDataEventListener(new DefaultAuditorAware());
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
        final AuditDataEventListener listener = new AuditDataEventListener(new DefaultAuditorAware());
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