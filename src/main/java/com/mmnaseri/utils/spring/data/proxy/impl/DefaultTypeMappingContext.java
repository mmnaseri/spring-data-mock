package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.commons.DefaultCrudRepository;
import com.mmnaseri.utils.spring.data.commons.DefaultPagingAndSortingRepository;
import com.mmnaseri.utils.spring.data.proxy.TypeMapping;
import com.mmnaseri.utils.spring.data.proxy.TypeMappingContext;
import org.springframework.core.OrderComparator;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/8/15)
 */
public class DefaultTypeMappingContext implements TypeMappingContext {

    private final TypeMappingContext parent;
    private final ConcurrentMap<Class<?>, List<Class<?>>> mappings = new ConcurrentHashMap<Class<?>, List<Class<?>>>();

    public DefaultTypeMappingContext() {
        this(null);
        register(Object.class, DefaultPagingAndSortingRepository.class);
        register(Object.class, DefaultCrudRepository.class);
    }

    public DefaultTypeMappingContext(TypeMappingContext parent) {
        this.parent = parent;
    }

    @Override
    public void register(Class<?> repositoryType, Class<?> implementation) {
        mappings.putIfAbsent(repositoryType, new CopyOnWriteArrayList<Class<?>>());
        mappings.get(repositoryType).add(implementation);
    }

    @Override
    public List<Class<?>> getImplementations(Class<?> repositoryType) {
        final List<Class<?>> classes = new LinkedList<Class<?>>();
        for (Class<?> superType : mappings.keySet()) {
            if (superType.isAssignableFrom(repositoryType)) {
                classes.addAll(mappings.get(superType));
            }
        }
        Collections.sort(classes, new OrderComparator());
        if (parent != null) {
            classes.addAll(parent.getImplementations(repositoryType));
        }
        return classes;
    }

    @Override
    public List<TypeMapping<?>> getMappings(Class<?> repositoryType) {
        final List<TypeMapping<?>> typeMappings = new LinkedList<TypeMapping<?>>();
        final List<Class<?>> implementations = getImplementations(repositoryType);
        for (Class<?> implementation : implementations) {
            if (Modifier.isAbstract(implementation.getModifiers()) || Modifier.isInterface(implementation.getModifiers())) {
                throw new IllegalStateException("Cannot instantiate a non-concrete class");
            }
            final Object instance;
            try {
                instance = implementation.newInstance();
            } catch (InstantiationException e) {
                throw new IllegalStateException("Failed to instantiate an object of type " + implementation, e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Failed to access the constructor for " + implementation, e);
            }
            //noinspection unchecked
            typeMappings.add(new ImmutableTypeMapping<Object>((Class<Object>) implementation, instance));
        }
        return typeMappings;
    }

}
