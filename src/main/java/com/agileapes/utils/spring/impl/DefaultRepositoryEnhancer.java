package com.agileapes.utils.spring.impl;

import com.agileapes.utils.spring.RepositoryDescriptor;
import com.agileapes.utils.spring.RepositoryEnhancer;
import net.sf.cglib.proxy.Enhancer;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/22 AD, 18:10)
 */
public class DefaultRepositoryEnhancer implements RepositoryEnhancer {

    @Override
    public <E, K extends Serializable, R extends Repository<E, K>> R instantiate(RepositoryDescriptor<E, K, R> descriptor) {
        final RepositoryMethodInterceptor<E, K, R> callback = new RepositoryMethodInterceptor<E, K, R>(descriptor);
        return descriptor.getRepositoryType().cast(Enhancer.create(Object.class, new Class[]{descriptor.getRepositoryType()}, callback));
    }

}
