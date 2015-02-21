package com.agileapes.utils.spring.impl.interceptors;

import com.agileapes.utils.spring.InterfaceInterceptor;
import com.agileapes.utils.spring.RepositoryDescriptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.agileapes.utils.spring.tools.EntityTools.iterableToList;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/23 AD, 12:57)
 */
public class MongoDataInterfaceInterceptor<E, K extends Serializable> implements InterfaceInterceptor<E, K>, MongoRepository<E, K> {

    private final DefaultDataInterfaceInterceptor<E, K> interceptor;

    public MongoDataInterfaceInterceptor(Map<K, E> data, RepositoryDescriptor<E, K, ?> descriptor) {
        interceptor = new DefaultDataInterfaceInterceptor<E, K>(data, descriptor);
    }

    @Override
    public <S extends E> List<S> save(Iterable<S> entities) {
        return iterableToList(interceptor.save(entities));
    }

    @Override
    public <S extends E> S save(S entity) {
        return interceptor.save(entity);
    }

    @Override
    public E findOne(K k) {
        return interceptor.findOne(k);
    }

    @Override
    public boolean exists(K k) {
        return interceptor.exists(k);
    }

    @Override
    public List<E> findAll() {
        return iterableToList(interceptor.findAll());
    }

    @Override
    public Iterable<E> findAll(Iterable<K> ks) {
        return iterableToList(interceptor.findAll(ks));
    }

    @Override
    public long count() {
        return interceptor.count();
    }

    @Override
    public void delete(K k) {
        interceptor.delete(k);
    }

    @Override
    public void delete(E entity) {
        interceptor.delete(entity);
    }

    @Override
    public void delete(Iterable<? extends E> entities) {
        interceptor.delete(entities);
    }

    @Override
    public void deleteAll() {
        interceptor.deleteAll();
    }

    @Override
    public List<E> findAll(Sort sort) {
        return iterableToList(interceptor.findAll(sort));
    }

    @Override
    public Page<E> findAll(Pageable pageable) {
        return interceptor.findAll(pageable);
    }

}
