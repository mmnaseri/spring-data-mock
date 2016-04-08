package com.mmnaseri.utils.spring.data.commons;

import com.mmnaseri.utils.spring.data.error.EntityMissingKeyException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/13/15)
 */
@SuppressWarnings("unchecked")
public class DefaultJpaRepository extends AbstractCrudRepository {

    public void flush() {
    }

    public Iterable deleteInBatch(Iterable entities) {
        final List result = new LinkedList();
        for (Object entity : entities) {
            final BeanWrapper wrapper = new BeanWrapperImpl(entity);
            final Object key = wrapper.getPropertyValue(getRepositoryMetadata().getIdentifierProperty());
            if (key == null) {
                throw new EntityMissingKeyException(getRepositoryMetadata().getEntityType(), getRepositoryMetadata().getIdentifierProperty());
            }
            final Serializable serializable = (Serializable) key;
            if (getDataStore().hasKey(serializable)) {
                result.add(getDataStore().retrieve(serializable));
                getDataStore().delete(serializable);
            }
        }
        return result;
    }

    public Iterable deleteAllInBatch() {
        return deleteInBatch(getDataStore().retrieveAll());
    }

    public Object getOne(Serializable serializable) {
        if (getDataStore().hasKey(serializable)) {
            return getDataStore().retrieve(serializable);
        }
        return null;
    }

    public Object saveAndFlush(Object entity) {
        final Object saved = save(entity);
        flush();
        return saved;
    }

}
