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
import java.util.Arrays;
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

    private final Map<Class<? extends Serializable>, List<Class<? extends KeyGenerator>>> generators;

    public KeyGeneratorProvider() {
        final List<Class<? extends KeyGenerator>> discoveredKeyGenerators = getKeyGeneratorTypes();
        generators = new ConcurrentHashMap<>();
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

    private List<Class<? extends KeyGenerator>> getKeyGeneratorTypes() {
        return Arrays.<Class<? extends KeyGenerator>>asList(
                RandomIntegerKeyGenerator.class,
                RandomLongKeyGenerator.class,
                SequentialIntegerKeyGenerator.class,
                SequentialLongKeyGenerator.class,
                UUIDKeyGenerator.class
        );
    }

    @SuppressWarnings("unchecked")
    private <S extends Serializable> List<Class<? extends KeyGenerator<S>>> getKeyGenerators(Class<S> keyType) {
        final LinkedList<Class<? extends KeyGenerator<S>>> keyGenerators = new LinkedList<Class<? extends KeyGenerator<S>>>();
        if (generators.containsKey(keyType)) {
            final List<Class<? extends KeyGenerator>> classes = generators.get(keyType);
            for (Class<? extends KeyGenerator> type : classes) {
                keyGenerators.add((Class<? extends KeyGenerator<S>>) type);
            }
        }
        for (Class<? extends Serializable> generatorKeyType : generators.keySet()) {
            if (keyType.isAssignableFrom(generatorKeyType)) {
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
