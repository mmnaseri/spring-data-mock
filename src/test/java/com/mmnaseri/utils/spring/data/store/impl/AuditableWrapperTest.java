package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableRepositoryMetadata;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AuditorAware;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/9/16)
 */
@SuppressWarnings("WeakerAccess, unused")
public class AuditableWrapperTest {

    private static final String AUDITOR = "AUDITOR";

    private static class SampleAuditorAware implements AuditorAware<String> {

        @Override
        public String getCurrentAuditor() {
            return AUDITOR;
        }

    }

    private static class EntityWithCreatedBy {

        private String id;
        @CreatedBy
        private String createdBy;

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

    }

    private static class EntityWithLastModifiedBy {

        private String id;
        @LastModifiedBy
        private String lastModifiedBy;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLastModifiedBy() {
            return lastModifiedBy;
        }

        public void setLastModifiedBy(String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
        }

    }

    private static class EntityWithCreatedDate {

        private String id;
        @CreatedDate
        private Date createdDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Date getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Date createdDate) {
            this.createdDate = createdDate;
        }

    }

    private static class EntityWithUtilDateLastModifiedDate {

        private String id;
        @LastModifiedDate
        private Date lastModifiedDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Date getLastModifiedDate() {
            return lastModifiedDate;
        }

        public void setLastModifiedDate(Date lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
        }
    }

    private static class EntityWithSqlDateLastModifiedDate {

        private String id;
        @LastModifiedDate
        private java.sql.Date lastModifiedDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public java.sql.Date getLastModifiedDate() {
            return lastModifiedDate;
        }

        public void setLastModifiedDate(java.sql.Date lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
        }

    }

    private static class EntityWithTimestampDateLastModifiedDate {

        private String id;
        @LastModifiedDate
        private Timestamp lastModifiedDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Timestamp getLastModifiedDate() {
            return lastModifiedDate;
        }

        public void setLastModifiedDate(Timestamp lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
        }
    }

    private static class EntityWithTimeLastModifiedDate {

        private String id;
        @LastModifiedDate
        private Time lastModifiedDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Time getLastModifiedDate() {
            return lastModifiedDate;
        }

        public void setLastModifiedDate(Time lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
        }

    }

    private static class EntityWithDateTimeLastModifiedDate {

        private String id;
        @LastModifiedDate
        private DateTime lastModifiedDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public DateTime getLastModifiedDate() {
            return lastModifiedDate;
        }

        public void setLastModifiedDate(DateTime lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
        }

    }

    private static class EntityWithFinalCreatedBy {

        private String id;
        @CreatedBy
        private final String createdBy = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreatedBy() {
            return createdBy;
        }
    }

    private static class EntityWithWriteOnlyCreatedBy {

        private String id;
        @CreatedBy
        private String createdBy;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

    }

    private AuditorAware<?> auditorAware;

    @BeforeMethod
    public void setUp() throws Exception {
        auditorAware = new SampleAuditorAware();
    }

    @Test
    public void testCreatedBy() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithCreatedBy.class, null, "id");
        final EntityWithCreatedBy entity = new EntityWithCreatedBy();
        final AuditableWrapper wrapper = new AuditableWrapper(entity, repositoryMetadata);
        assertThat(entity.getCreatedBy(), is(nullValue()));
        assertThat(wrapper.getCreatedBy(), is(nullValue()));
        wrapper.setCreatedBy(auditorAware.getCurrentAuditor());
        assertThat(wrapper.getCreatedBy(), Matchers.<Object>is(AUDITOR));
        assertThat(entity.getCreatedBy(), is(AUDITOR));
    }

    @Test
    public void testLastModifiedBy() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithLastModifiedBy.class, null, "id");
        final EntityWithLastModifiedBy entity = new EntityWithLastModifiedBy();
        final AuditableWrapper wrapper = new AuditableWrapper(entity, repositoryMetadata);
        assertThat(entity.getLastModifiedBy(), is(nullValue()));
        assertThat(wrapper.getLastModifiedBy(), is(nullValue()));
        wrapper.setLastModifiedBy(auditorAware.getCurrentAuditor());
        assertThat(wrapper.getLastModifiedBy(), Matchers.<Object>is(AUDITOR));
        assertThat(entity.getLastModifiedBy(), is(AUDITOR));
    }

    @Test
    public void testCreatedDate() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithCreatedDate.class, null, "id");
        final EntityWithCreatedDate entity = new EntityWithCreatedDate();
        final AuditableWrapper wrapper = new AuditableWrapper(entity, repositoryMetadata);
        assertThat(entity.getCreatedDate(), is(nullValue()));
        assertThat(wrapper.getCreatedDate(), is(nullValue()));
        final DateTime time = new DateTime();
        wrapper.setCreatedDate(time);
        assertThat(wrapper.getCreatedDate(), is(time));
        assertThat(entity.getCreatedDate(), is(time.toDate()));
    }

    @Test
    public void testUtilDateLastModifiedDate() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithUtilDateLastModifiedDate.class, null, "id");
        final EntityWithUtilDateLastModifiedDate entity = new EntityWithUtilDateLastModifiedDate();
        final AuditableWrapper wrapper = new AuditableWrapper(entity, repositoryMetadata);
        final DateTime time = new DateTime();
        assertThat(entity.getLastModifiedDate(), is(nullValue()));
        assertThat(wrapper.getLastModifiedDate(), is(nullValue()));
        wrapper.setLastModifiedDate(time);
        assertThat(wrapper.getLastModifiedDate(), is(time));
        assertThat(entity.getLastModifiedDate(), is(time.toDate()));
    }

    @Test
    public void testSqlDateLastModifiedDate() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithSqlDateLastModifiedDate.class, null, "id");
        final EntityWithSqlDateLastModifiedDate entity = new EntityWithSqlDateLastModifiedDate();
        final AuditableWrapper wrapper = new AuditableWrapper(entity, repositoryMetadata);
        final DateTime time = new DateTime();
        assertThat(entity.getLastModifiedDate(), is(nullValue()));
        assertThat(wrapper.getLastModifiedDate(), is(nullValue()));
        wrapper.setLastModifiedDate(time);
        assertThat(wrapper.getLastModifiedDate(), is(time));
        assertThat(entity.getLastModifiedDate(), is(new java.sql.Date(time.toDate().getTime())));
    }

    @Test
    public void testTimestampLastModifiedDate() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithTimestampDateLastModifiedDate.class, null, "id");
        final EntityWithTimestampDateLastModifiedDate entity = new EntityWithTimestampDateLastModifiedDate();
        final AuditableWrapper wrapper = new AuditableWrapper(entity, repositoryMetadata);
        final DateTime time = new DateTime();
        assertThat(entity.getLastModifiedDate(), is(nullValue()));
        assertThat(wrapper.getLastModifiedDate(), is(nullValue()));
        wrapper.setLastModifiedDate(time);
        assertThat(wrapper.getLastModifiedDate(), is(time));
        assertThat(entity.getLastModifiedDate(), is(new Timestamp(time.toDate().getTime())));
    }

    @Test
    public void testTimeLastModifiedDate() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithTimeLastModifiedDate.class, null, "id");
        final EntityWithTimeLastModifiedDate entity = new EntityWithTimeLastModifiedDate();
        final AuditableWrapper wrapper = new AuditableWrapper(entity, repositoryMetadata);
        final DateTime time = new DateTime();
        assertThat(entity.getLastModifiedDate(), is(nullValue()));
        assertThat(wrapper.getLastModifiedDate(), is(nullValue()));
        wrapper.setLastModifiedDate(time);
        assertThat(wrapper.getLastModifiedDate(), is(time));
        assertThat(entity.getLastModifiedDate(), is(new Time(time.toDate().getTime())));
    }

    @Test
    public void testDateTimeLastModifiedDate() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithDateTimeLastModifiedDate.class, null, "id");
        final EntityWithDateTimeLastModifiedDate entity = new EntityWithDateTimeLastModifiedDate();
        final AuditableWrapper wrapper = new AuditableWrapper(entity, repositoryMetadata);
        final DateTime time = new DateTime();
        assertThat(entity.getLastModifiedDate(), is(nullValue()));
        assertThat(wrapper.getLastModifiedDate(), is(nullValue()));
        wrapper.setLastModifiedDate(time);
        assertThat(wrapper.getLastModifiedDate(), is(time));
        assertThat(entity.getLastModifiedDate(), is(time));
    }

    @Test
    public void testSettingNullValue() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithCreatedBy.class, null, "id");
        final EntityWithCreatedBy entity = new EntityWithCreatedBy();
        final AuditableWrapper wrapper = new AuditableWrapper(entity, repositoryMetadata);
        assertThat(entity.getCreatedBy(), is(nullValue()));
        wrapper.setCreatedBy(null);
        assertThat(entity.getCreatedBy(), is(nullValue()));
    }

    @Test
    public void testGettingIdentifier() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithCreatedBy.class, null, "id");
        final EntityWithCreatedBy entity = new EntityWithCreatedBy();
        final AuditableWrapper wrapper = new AuditableWrapper(entity, repositoryMetadata);
        assertThat(wrapper.getId(), is(nullValue()));
        entity.setId("some identifier");
        assertThat(wrapper.getId(), is(notNullValue()));
        assertThat(wrapper.getId(), Matchers.<Serializable>is(entity.getId()));
    }

    @Test
    public void testDirtyChecking() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithCreatedBy.class, null, "id");
        final EntityWithCreatedBy entity = new EntityWithCreatedBy();
        final AuditableWrapper wrapper = new AuditableWrapper(entity, repositoryMetadata);
        assertThat(wrapper.isNew(), is(true));
        entity.setId("some identifier");
        assertThat(wrapper.isNew(), is(false));
    }

    @Test
    public void testSettingReadOnlyProperty() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithFinalCreatedBy.class, null, "id");
        final EntityWithFinalCreatedBy entity = new EntityWithFinalCreatedBy();
        final AuditableWrapper wrapper = new AuditableWrapper(entity, repositoryMetadata);
        assertThat(entity.getCreatedBy(), is(nullValue()));
        assertThat(wrapper.getCreatedBy(), is(nullValue()));
        wrapper.setCreatedBy(auditorAware.getCurrentAuditor());
        assertThat(wrapper.getCreatedBy(), is(nullValue()));
        assertThat(entity.getCreatedBy(), is(nullValue()));
    }

    @Test
    public void testWriteOnlyCreatedBy() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, EntityWithWriteOnlyCreatedBy.class, null, "id");
        final EntityWithWriteOnlyCreatedBy entity = new EntityWithWriteOnlyCreatedBy();
        final AuditableWrapper wrapper = new AuditableWrapper(entity, repositoryMetadata);
        assertThat(entity.createdBy, is(nullValue()));
        assertThat(wrapper.getCreatedBy(), is(nullValue()));
        wrapper.setCreatedBy(auditorAware.getCurrentAuditor());
        //the wrapper will not be able to read the value ...
        assertThat(wrapper.getCreatedBy(), Matchers.is(nullValue()));
        //... but the value is set anyway
        assertThat(entity.createdBy, is(AUDITOR));
    }

}