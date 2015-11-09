package com.mmnaseri.utils.spring.data.domain.impl.key;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import org.springframework.asm.ClassReader;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor;
import org.springframework.util.ClassUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/12/15)
 */
public class KeyGeneratorProvider {

    public static final String BASE_PACKAGE = "com.mmnaseri.utils.spring.data.domain.impl.key";
    private final Map<Class<? extends Serializable>, List<Class<? extends KeyGenerator>>> generators;

    public KeyGeneratorProvider() {
        final List<Class<? extends KeyGenerator>> discoveredKeyGenerators = getKeyGeneratorTypes();
        generators = new ConcurrentHashMap<Class<? extends Serializable>, List<Class<? extends KeyGenerator>>>();
        for (Class<? extends KeyGenerator> generatorType : discoveredKeyGenerators) {
            final Class<?> keyType = GenericTypeResolver.resolveTypeArgument(generatorType, KeyGenerator.class);
            if (keyType == null) {
                continue;
            }
            final Class<? extends Serializable> actualKeyType = keyType.asSubclass(Serializable.class);
            if (!generators.containsKey(actualKeyType)) {
                generators.put(actualKeyType, new CopyOnWriteArrayList<Class<? extends KeyGenerator>>());
            }
            generators.get(actualKeyType).add(generatorType);
        }
    }

    protected List<Class<? extends KeyGenerator>> getKeyGeneratorTypes() {
        final ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        final PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(defaultClassLoader);
        final Resource[] resources;
        try {
            resources = resourcePatternResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + BASE_PACKAGE.replace('.', '/') + "/**/*.class");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load resources", e);
        }
        final List<Class<? extends KeyGenerator>> discoveredKeyGenerators = new LinkedList<Class<? extends KeyGenerator>>();
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                final InputStream inputStream;
                try {
                    inputStream = new BufferedInputStream(resource.getInputStream());
                } catch (IOException e) {
                    continue;
                }
                final ClassReader classReader;
                try {
                    classReader = new ClassReader(inputStream);
                } catch (IOException e) {
                    continue;
                }
                final AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor(defaultClassLoader);
                classReader.accept(visitor, ClassReader.SKIP_DEBUG);
                final Class<?> observedClass;
                try {
                    observedClass = ClassUtils.forName(visitor.getClassName(), defaultClassLoader);
                } catch (ClassNotFoundException e) {
                    continue;
                }
                if (KeyGenerator.class.isAssignableFrom(observedClass)) {
                    discoveredKeyGenerators.add(observedClass.asSubclass(KeyGenerator.class));
                }
            }
        }
        return discoveredKeyGenerators;
    }

    @SuppressWarnings("unchecked")
    public <S extends Serializable> List<Class<? extends KeyGenerator<S>>> getKeyGenerators(Class<S> keyType) {
        final LinkedList<Class<? extends KeyGenerator<S>>> keyGenerators = new LinkedList<Class<? extends KeyGenerator<S>>>();
        if (generators.containsKey(keyType)) {
            final List<Class<? extends KeyGenerator>> classes = generators.get(keyType);
            for (Class<? extends KeyGenerator> type : classes) {
                keyGenerators.add((Class<? extends KeyGenerator<S>>) type);
            }
        }
        for (Class<? extends Serializable> generatorKeyType : generators.keySet()) {
            if (!generatorKeyType.equals(keyType) && keyType.isAssignableFrom(generatorKeyType)) {
                final List<Class<? extends KeyGenerator>> classes = generators.get(generatorKeyType);
                for (Class<? extends KeyGenerator> type : classes) {
                    keyGenerators.add((Class<? extends KeyGenerator<S>>) type);
                }
            }
        }
        return keyGenerators;
    }

    public <S extends Serializable> Class<? extends KeyGenerator<S>> getKeyGenerator(Class<S> keyType) {
        final List<Class<? extends KeyGenerator<S>>> generators = getKeyGenerators(keyType);
        return generators.isEmpty() ? null : generators.get(0);
    }

}
