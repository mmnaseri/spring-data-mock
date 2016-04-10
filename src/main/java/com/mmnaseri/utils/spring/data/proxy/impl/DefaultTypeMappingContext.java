package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.commons.DefaultCrudRepository;
import com.mmnaseri.utils.spring.data.commons.DefaultGemfireRepository;
import com.mmnaseri.utils.spring.data.commons.DefaultJpaRepository;
import com.mmnaseri.utils.spring.data.commons.DefaultPagingAndSortingRepository;
import com.mmnaseri.utils.spring.data.error.RepositoryDefinitionException;
import com.mmnaseri.utils.spring.data.proxy.TypeMapping;
import com.mmnaseri.utils.spring.data.proxy.TypeMappingContext;
import org.springframework.core.OrderComparator;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/8/15)
 */
public class DefaultTypeMappingContext implements TypeMappingContext {

    private static class Mapping {

        private final Class<?> key;
        private final Class<?> value;

        public Mapping(Class<?> key, Class<?> value) {
            this.key = key;
            this.value = value;
        }

        public Class<?> getKey() {
            return key;
        }

        public Class<?> getValue() {
            return value;
        }

    }

    private final TypeMappingContext parent;
    private final List<Mapping> mappings = new LinkedList<Mapping>();

    public DefaultTypeMappingContext() {
        this(null);
        if (ClassUtils.isPresent("org.springframework.data.gemfire.repository.GemfireRepository", ClassUtils.getDefaultClassLoader())) {
            register(Object.class, DefaultGemfireRepository.class);
        }
        if (ClassUtils.isPresent("org.springframework.data.jpa.repository.JpaRepository", ClassUtils.getDefaultClassLoader())) {
            register(Object.class, DefaultJpaRepository.class);
        }
        register(Object.class, DefaultPagingAndSortingRepository.class);
        register(Object.class, DefaultCrudRepository.class);
    }

    public DefaultTypeMappingContext(TypeMappingContext parent) {
        this.parent = parent;
    }

    @Override
    public void register(Class<?> repositoryType, Class<?> implementation) {
        mappings.add(new Mapping(repositoryType, implementation));
    }

    @Override
    public List<Class<?>> getImplementations(Class<?> repositoryType) {
        final List<Class<?>> classes = new LinkedList<Class<?>>();
        for (Mapping mapping : mappings) {
            if (mapping.getKey().isAssignableFrom(repositoryType)) {
                classes.add(mapping.getValue());
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
                throw new RepositoryDefinitionException(repositoryType, "Cannot instantiate a non-concrete class");
            }
            final Object instance;
            try {
                instance = implementation.newInstance();
            } catch (InstantiationException e) {
                throw new RepositoryDefinitionException(repositoryType, "Failed to instantiate an object of type " + implementation, e);
            } catch (IllegalAccessException e) {
                throw new RepositoryDefinitionException(repositoryType, "Failed to access the constructor for " + implementation, e);
            }
            //noinspection unchecked
            typeMappings.add(new ImmutableTypeMapping<Object>((Class<Object>) implementation, instance));
        }
        return typeMappings;
    }

}
