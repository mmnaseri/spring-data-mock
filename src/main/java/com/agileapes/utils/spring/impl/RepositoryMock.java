/*
 * Copyright (c) 2014 Milad Naseri
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.agileapes.utils.spring.impl;

import com.agileapes.utils.spring.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.repository.Repository;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/23 AD, 13:03)
 */
public class RepositoryMock<E, K extends Serializable, R extends Repository<E, K>> {

    private final RepositoryDescriptor<E, K, R> descriptor;
    private final RepositoryEnhancer enhancer;

    private RepositoryMock(RepositoryDescriptor<E, K, R> descriptor) {
        this.descriptor = descriptor;
        this.enhancer = new DefaultRepositoryEnhancer();
    }
    
    @SuppressWarnings("unchecked")
    public RepositoryMock<E, K, R> withData(E... entities) {
        return withData(Arrays.asList(entities));
    }
    
    public RepositoryMock<E, K, R> withData(Iterable<E> entities) {
        final Map<K, E> data = new HashMap<K, E>(descriptor.getData());
        for (E entity : entities) {
            final BeanWrapper wrapper = new BeanWrapperImpl(entity);
            //noinspection unchecked
            final K key = (K) wrapper.getPropertyValue(descriptor.getKeyProperty());
            Objects.requireNonNull(key, "Key cannot be null");
            data.put(key, entity);
        }
        return new RepositoryMock<E, K, R>(new ImmutableRepositoryDescriptor<E, K, R>(descriptor.getRepositoryType(), data, descriptor.getOperations(), descriptor.getKeyProperty(), descriptor.getKeyGeneration()));
    }

    public RepositoryMock<E, K, R> withData(String location) {
        return withData(new PathMatchingResourcePatternResolver(ClassUtils.getDefaultClassLoader()).getResource(location));
    }

    public RepositoryMock<E, K, R> withData(Resource resource) {
        if (!resource.exists()) {
            throw new IllegalArgumentException("Resource does not exist");
        }
        final InputStream inputStream;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to access the input stream", e);
        }
        if (inputStream == null) {
            throw new IllegalArgumentException("Passed in input does not represent a valid input source. Perhaps the resource is within a JAR file?");
        }
        final ObjectMapper mapper = new ObjectMapper();
        final List<E> entities;
        try {
            //noinspection unchecked
            entities = mapper.readValue(inputStream, CollectionLikeType.construct(ArrayList.class, SimpleType.construct(descriptor.getEntityType())));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read the input document", e);
        }
        return withData(entities);
    }
    
    public RepositoryMock<E, K, R> withOperation(MethodDescriptor methodDescriptor, DataOperation<E, K> operation) {
        final Map<MethodDescriptor, DataOperation<E, K>> operations = new HashMap<MethodDescriptor, DataOperation<E, K>>(descriptor.getOperations());
        operations.put(methodDescriptor, operation);
        return new RepositoryMock<E, K, R>(new ImmutableRepositoryDescriptor<E, K, R>(descriptor.getRepositoryType(), descriptor.getData(), operations, descriptor.getKeyProperty(), descriptor.getKeyGeneration()));
    }
    
    public RepositoryMock<E, K, R> withOperation(Method method, DataOperation<E, K> operation) {
        final Map<MethodDescriptor, DataOperation<E, K>> operations = new HashMap<MethodDescriptor, DataOperation<E, K>>(descriptor.getOperations());
        operations.put(new ImmutableMethodDescriptor(method.getDeclaringClass(), method.getName(), method.getGenericReturnType(), method.getGenericParameterTypes()), operation);
        return new RepositoryMock<E, K, R>(new ImmutableRepositoryDescriptor<E, K, R>(descriptor.getRepositoryType(), descriptor.getData(), operations, descriptor.getKeyProperty(), descriptor.getKeyGeneration()));
    }

    public R mock() {
        return enhancer.instantiate(descriptor);
    }

    public static <E, K extends Serializable, R extends Repository<E, K>> RepositoryMock<E, K, R> forRepository(Class<R> repositoryType, String key) {
        return forRepository(repositoryType, key, KeyGeneration.NONE);
    }

    public static <E, K extends Serializable, R extends Repository<E, K>> RepositoryMock<E, K, R> forRepository(Class<R> repositoryType, String key, KeyGeneration keyGeneration) {
        return new RepositoryMock<E, K, R>(new ImmutableRepositoryDescriptor<E, K, R>(repositoryType, new HashMap<K, E>(), new HashMap<MethodDescriptor, DataOperation<E, K>>(), key, keyGeneration));
    }
    
}
