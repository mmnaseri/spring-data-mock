package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.tools.GetterMethodFilter;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Optional;

/**
 * This class is used to wrap a normal entity in an entity that supports {@link Auditable auditing}. If the underlying
 * entity does not exhibit auditable behavior (either by implementing {@link Auditable} or by having properties that are
 * annotated with one of the audit annotations provided by Spring Data Commons) this class will simply ignore audit
 * requests. Otherwise, it will convert the values to their appropriate types and sets and gets the appropriate
 * properties.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/12/15)
 */
@SuppressWarnings("WeakerAccess")
public class AuditableWrapper implements Auditable {

    private final BeanWrapper wrapper;
    private final RepositoryMetadata repositoryMetadata;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;

    private static String findProperty(Class<?> entityType, Class<? extends Annotation> annotationType) {
        final PropertyVisitor visitor = new PropertyVisitor(annotationType);
        ReflectionUtils.doWithFields(entityType, visitor);
        ReflectionUtils.doWithMethods(entityType, visitor, new GetterMethodFilter());
        return visitor.getProperty();
    }

    /**
     * Returns the property value for a given audit property.
     *
     * @param type     the type of the property
     * @param wrapper  the bean wrapper
     * @param property actual property to read
     * @param <E>      the object type for the value
     * @return the property value
     */
    private static <E> E getProperty(Class<E> type, BeanWrapper wrapper, String property) {
        if (property == null || !wrapper.isReadableProperty(property)) {
            return null;
        }
        Object propertyValue = wrapper.getPropertyValue(property);
        if (propertyValue instanceof Date) {
            propertyValue = Instant.ofEpochMilli(((Date) propertyValue).getTime());
        }
        return type.cast(propertyValue);
    }

    /**
     * Sets an audit property on the given entity
     *
     * @param wrapper  the bean wrapper for the entity
     * @param property the property for which the value is being set
     * @param value    the original value of the property
     */
    private static void setProperty(BeanWrapper wrapper, String property, Object value) {
        if (property != null) {
            Object targetValue = value;
            final PropertyDescriptor descriptor = wrapper.getPropertyDescriptor(property);
            if (targetValue instanceof Instant) {
                Instant instant = (Instant) targetValue;
                if (Date.class.equals(descriptor.getPropertyType())) {
                    targetValue = new Date(instant.toEpochMilli());
                } else if (java.sql.Date.class.equals(descriptor.getPropertyType())) {
                    targetValue = new java.sql.Date(instant.toEpochMilli());
                } else if (java.sql.Timestamp.class.equals(descriptor.getPropertyType())) {
                    targetValue = new java.sql.Timestamp(instant.toEpochMilli());
                } else if (java.sql.Time.class.equals(descriptor.getPropertyType())) {
                    targetValue = new java.sql.Time(instant.toEpochMilli());
                }
            }
            if (wrapper.isWritableProperty(property)) {
                wrapper.setPropertyValue(property, targetValue);
            }
        }
    }

    public AuditableWrapper(Object entity, RepositoryMetadata repositoryMetadata) {
        this.repositoryMetadata = repositoryMetadata;
        this.wrapper = new BeanWrapperImpl(entity);
        this.createdBy = findProperty(repositoryMetadata.getEntityType(), CreatedBy.class);
        this.createdDate = findProperty(repositoryMetadata.getEntityType(), CreatedDate.class);
        this.lastModifiedBy = findProperty(repositoryMetadata.getEntityType(), LastModifiedBy.class);
        this.lastModifiedDate = findProperty(repositoryMetadata.getEntityType(), LastModifiedDate.class);
    }

    @Override
    public Optional<Object> getCreatedBy() {
        return Optional.ofNullable(getProperty(Object.class, wrapper, createdBy));
    }

    @Override
    public void setCreatedBy(Object createdBy) {
        setProperty(wrapper, this.createdBy, createdBy);
    }

    @Override
    public Optional<Instant> getCreatedDate() {
        return Optional.ofNullable(getProperty(Instant.class, wrapper, createdDate));
    }

    @Override
    public void setCreatedDate(TemporalAccessor creationDate) {
        setProperty(wrapper, createdDate, creationDate);
    }

    @Override
    public Optional<Object> getLastModifiedBy() {
        return Optional.ofNullable(getProperty(Object.class, wrapper, lastModifiedBy));
    }

    @Override
    public void setLastModifiedBy(Object lastModifiedBy) {
        setProperty(wrapper, this.lastModifiedBy, lastModifiedBy);
    }

    @Override
    public Optional<Instant> getLastModifiedDate() {
        return Optional.ofNullable(getProperty(Instant.class, wrapper, lastModifiedDate));
    }

    @Override
    public void setLastModifiedDate(TemporalAccessor lastModifiedDate) {
        setProperty(wrapper, this.lastModifiedDate, lastModifiedDate);
    }

    @Override
    public Object getId() {
        return wrapper.getPropertyValue(repositoryMetadata.getIdentifierProperty());
    }

    @Override
    public boolean isNew() {
        return getId() == null;
    }

}
