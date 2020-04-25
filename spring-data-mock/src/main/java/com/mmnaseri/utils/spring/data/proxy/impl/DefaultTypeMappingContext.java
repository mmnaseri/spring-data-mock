package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.error.RepositoryDefinitionException;
import com.mmnaseri.utils.spring.data.proxy.TypeMapping;
import com.mmnaseri.utils.spring.data.proxy.TypeMappingContext;
import com.mmnaseri.utils.spring.data.repository.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>This is the default type mapping context that is also capable of registering the default mappings
 * for the interfaces provided through Spring Data.</p>
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/8/15)
 */
@SuppressWarnings("WeakerAccess")
public class DefaultTypeMappingContext implements TypeMappingContext {

    private static final Log log = LogFactory.getLog(DefaultTypeMappingContext.class);
    private static final String GEMFIRE_SUPPORT_CLASS = "org.springframework.data.gemfire.repository.GemfireRepository";
    private static final String JPA_SUPPORT_CLASS = "org.springframework.data.jpa.repository.JpaRepository";
    private static final String QUERY_BY_EXAMPLE_SUPPORT_CLASS =
            "org.springframework.data.repository.query.QueryByExampleExecutor";
    private final TypeMappingContext parent;
    private ConcurrentMap<Class<?>, List<Class<?>>> mappings = new ConcurrentHashMap<>();

    /**
     * Instantiates the context and registers all the default converters
     */
    public DefaultTypeMappingContext() {
        this(true);
    }

    /**
     * Instantiates the context
     *
     * @param registerDefaults whether or not the default mappings should be registered.
     */
    public DefaultTypeMappingContext(boolean registerDefaults) {
        this(null);
        if (registerDefaults) {
            log.info("Trying to register all the default type mappings");
            if (isClassPresent(GEMFIRE_SUPPORT_CLASS)) {
                log.debug("We seem to have Gemfire in the classpath, so, we should register the supporting registry");
                register(Object.class, DefaultGemfireRepository.class);
            }
            if (isClassPresent(JPA_SUPPORT_CLASS)) {
                log.debug("JPA support is enabled in this project, so we need to support the methods");
                register(Object.class, DefaultJpaRepository.class);
            }
            if (isClassPresent(QUERY_BY_EXAMPLE_SUPPORT_CLASS)) {
                log.debug("Query by example is enabled. We will the proper method implementations.");
                register(Object.class, DefaultQueryByExampleExecutor.class);
            }
            register(Object.class, DefaultPagingAndSortingRepository.class);
            register(Object.class, DefaultCrudRepository.class);
        }
    }

    private boolean isClassPresent(String className) {
        return ClassUtils.isPresent(className, ClassUtils.getDefaultClassLoader());
    }

    public DefaultTypeMappingContext(TypeMappingContext parent) {
        this.parent = parent;
    }

    @Override
    public void register(Class<?> repositoryType, Class<?> implementation) {
        if (Modifier.isAbstract(implementation.getModifiers()) || Modifier.isInterface(implementation.getModifiers())) {
            log.error("Cannot bind a non-concrete class as an implementation for a non-concrete class");
            throw new RepositoryDefinitionException(repositoryType,
                                                    "Cannot bind a non-concrete class as an implementation for a "
                                                            + "non-concrete class");
        }
        log.info("Registering implementation " + implementation + " to super type " + repositoryType +
                         "; this means any repository of this type will inherit functionality defined in the " +
                         "bound implementation class.");
        mappings.putIfAbsent(repositoryType, new LinkedList<Class<?>>());
        mappings.get(repositoryType).add(implementation);
    }

    @Override
    public List<Class<?>> getImplementations(Class<?> repositoryType) {
        final List<Class<?>> classes = new LinkedList<>();
        for (Class<?> repositorySuperType : mappings.keySet()) {
            if (repositorySuperType.isAssignableFrom(repositoryType)) {
                classes.addAll(mappings.get(repositorySuperType));
            }
        }
        Collections.sort(classes, AnnotationAwareOrderComparator.INSTANCE);
        if (parent != null) {
            classes.addAll(parent.getImplementations(repositoryType));
        }
        return classes;
    }

    @Override
    public List<TypeMapping<?>> getMappings(Class<?> repositoryType) {
        final List<TypeMapping<?>> typeMappings = new LinkedList<>();
        final List<Class<?>> implementations = getImplementations(repositoryType);
        log.info("The repository " + repositoryType + " is bound to implementations " + implementations);
        for (Class<?> implementation : implementations) {
            final Object instance;
            try {
                instance = implementation.newInstance();
            } catch (InstantiationException e) {
                log.error("Failed to instantiate class " + implementation
                                  + " because there was an error in the constructor");
                throw new RepositoryDefinitionException(repositoryType,
                                                        "Failed to instantiate an object of type " + implementation, e);
            } catch (IllegalAccessException e) {
                log.error("The constructor for the implementation class is not accessible: " + implementation);
                throw new RepositoryDefinitionException(repositoryType,
                                                        "Failed to access the constructor for " + implementation, e);
            } catch (Exception e) {
                log.error("The constructor for " + implementation + " threw an exception");
                throw new RepositoryDefinitionException(repositoryType,
                                                        "Constructor threw an exception " + implementation, e);
            }
            //noinspection unchecked
            typeMappings.add(new ImmutableTypeMapping<>((Class<Object>) implementation, instance));
        }
        return typeMappings;
    }

}
