package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.tools.GetterMethodFilter;
import org.joda.time.DateTime;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Date;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/12/15)
 */
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

    private static <E> E getProperty(Class<E> type, BeanWrapper wrapper, String property) {
        if (property == null || !wrapper.isReadableProperty(property)) {
            return null;
        }
        Object propertyValue = wrapper.getPropertyValue(property);
        if (propertyValue instanceof Date) {
            propertyValue = new DateTime(((Date) propertyValue).getTime());
        }
        return type.cast(propertyValue);
    }

    private static void setProperty(BeanWrapper wrapper, String property, Object value) {
        if (property != null) {
            Object targetValue = value;
            final PropertyDescriptor descriptor = wrapper.getPropertyDescriptor(property);
            if (targetValue instanceof DateTime) {
                DateTime dateTime = (DateTime) targetValue;
                if (Date.class.equals(descriptor.getPropertyType())) {
                    targetValue = dateTime.toDate();
                } else if (java.sql.Date.class.equals(descriptor.getPropertyType())) {
                    targetValue = new java.sql.Date(dateTime.toDate().getTime());
                } else if (java.sql.Timestamp.class.equals(descriptor.getPropertyType())) {
                    targetValue = new java.sql.Timestamp(dateTime.toDate().getTime());
                } else if (java.sql.Time.class.equals(descriptor.getPropertyType())) {
                    targetValue = new java.sql.Time(dateTime.toDate().getTime());
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
    public Object getCreatedBy() {
        return getProperty(Object.class, wrapper, createdBy);
    }

    @Override
    public void setCreatedBy(Object createdBy) {
        setProperty(wrapper, this.createdBy, createdBy);
    }

    @Override
    public DateTime getCreatedDate() {
        return getProperty(DateTime.class, wrapper, createdDate);
    }

    @Override
    public void setCreatedDate(DateTime creationDate) {
        setProperty(wrapper, createdDate, creationDate);
    }

    @Override
    public Object getLastModifiedBy() {
        return getProperty(Object.class, wrapper, lastModifiedBy);
    }

    @Override
    public void setLastModifiedBy(Object lastModifiedBy) {
        setProperty(wrapper, this.lastModifiedBy, lastModifiedBy);
    }

    @Override
    public DateTime getLastModifiedDate() {
        return getProperty(DateTime.class, wrapper, lastModifiedDate);
    }

    @Override
    public void setLastModifiedDate(DateTime lastModifiedDate) {
        setProperty(wrapper, this.lastModifiedDate, lastModifiedDate);
    }

    @Override
    public Serializable getId() {
        return (Serializable) wrapper.getPropertyValue(repositoryMetadata.getIdentifierProperty());
    }

    @Override
    public boolean isNew() {
        return getId() == null;
    }

}
