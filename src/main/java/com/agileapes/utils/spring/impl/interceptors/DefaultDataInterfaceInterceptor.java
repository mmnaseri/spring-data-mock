package com.agileapes.utils.spring.impl.interceptors;

import com.agileapes.utils.spring.InterfaceInterceptor;
import com.agileapes.utils.spring.KeyGeneration;
import com.agileapes.utils.spring.RepositoryDescriptor;
import com.agileapes.utils.spring.tools.EntityTools;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/22 AD, 19:40)
 */
public class DefaultDataInterfaceInterceptor<E, K extends Serializable> implements PagingAndSortingRepository<E, K>, InterfaceInterceptor<E, K> {

    private final Map<K, E> data;
    private final RepositoryDescriptor<E, K, ?> descriptor;
    private final Random random;

    public DefaultDataInterfaceInterceptor(Map<K, E> data, RepositoryDescriptor<E, K, ?> descriptor) {
        this.data = data;
        this.descriptor = descriptor;
        random = new Random();
    }

    @SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
    private K nextKey() {
        String stringKey = "";
        Long longKey = 1L;
        Integer intKey = 1;
        switch (descriptor.getKeyGeneration()) {
            case STRING_RANDOM:
                while (stringKey.length() < 64) {
                    stringKey += Long.toHexString(random.nextLong());
                }
                return (K) stringKey;
            case LONG_SEQUENTIAL:
                for (K key : data.keySet()) {
                    if (longKey < (Long) key) {
                        longKey = (Long) key;
                    }
                }
                return (K) (Long) (longKey + 1L);
            case LONG_RANDOM:
                do {
                    longKey = random.nextLong();
                } while (data.containsKey(longKey));
                return (K) longKey;
            case INT_SEQUENTIAL:
                for (K key : data.keySet()) {
                    if (intKey < (Integer) key) {
                        intKey = (Integer) key;
                    }
                }
                return (K) (Integer) (intKey + 1);
            case INT_RANDOM:
                do {
                    intKey = random.nextInt();
                } while (data.containsKey(intKey));
                return (K) intKey;
        }
        return null;
    }

    private K getKey(E entity) {
        final BeanWrapper wrapper = new BeanWrapperImpl(entity);
        //noinspection unchecked
        return (K) wrapper.getPropertyValue(descriptor.getKeyProperty());
    }

    private void setKey(E entity, K value) {
        final BeanWrapper wrapper = new BeanWrapperImpl(entity);
        wrapper.setPropertyValue(descriptor.getKeyProperty(), value);
    }

    @Override
    public <S extends E> S save(S entity) {
        K key = getKey(entity);
        if (key == null) {
            if (KeyGeneration.NONE.equals(descriptor.getKeyGeneration())) {
                throw new IllegalArgumentException();
            } else {
                setKey(entity, nextKey());
            }
            key = getKey(entity);
        }
        data.put(key, entity);
        return entity;
    }

    @Override
    public <S extends E> Iterable<S> save(Iterable<S> entities) {
        for (S entity : entities) {
            save(entity);
        }
        return entities;
    }

    @Override
    public E findOne(K key) {
        if (data.containsKey(key)) {
            return data.get(key);
        }
        return null;
    }

    @Override
    public boolean exists(K key) {
        return data.containsKey(key);
    }

    @Override
    public Iterable<E> findAll() {
        return new ArrayList<E>(data.values());
    }

    @Override
    public Iterable<E> findAll(Iterable<K> keys) {
        final ArrayList<E> result = new ArrayList<E>();
        for (K key : keys) {
            if (data.containsKey(key)) {
                result.add(data.get(key));
            }
        }
        return result;
    }

    @Override
    public long count() {
        return data.size();
    }

    @Override
    public void delete(K key) {
        Objects.requireNonNull(key);
        data.remove(key);
    }

    @Override
    public void delete(E entity) {
        delete(getKey(entity));
    }

    @Override
    public void delete(Iterable<? extends E> entities) {
        for (E entity : entities) {
            delete(entity);
        }
    }

    @Override
    public void deleteAll() {
        data.clear();
    }

    @Override
    public Iterable<E> findAll(Sort sort) {
        final ArrayList<E> entities = new ArrayList<E>(data.values());
        EntityTools.sort(entities, sort);
        return entities;
    }

    @Override
    public Page<E> findAll(Pageable pageable) {
        if (pageable.getOffset() >= data.size()) {
            return new PageImpl<E>(new ArrayList<E>());
        }
        final ArrayList<E> entities = new ArrayList<E>(data.values());
        EntityTools.sort(entities, pageable.getSort());
        final List<E> content = entities.subList(pageable.getOffset(), Math.min(pageable.getOffset() + pageable.getPageSize(), entities.size()));
        return new PageImpl<E>(content, pageable, entities.size());
    }
    
}
