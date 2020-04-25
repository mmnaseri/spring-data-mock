package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.domain.*;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.query.impl.DefaultQueryDescriptor;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/8/16, 12:45 PM)
 */
public class ExampleMatcherQueryDescriptionExtractor implements QueryDescriptionExtractor<Example> {

    @Override
    public QueryDescriptor extract(RepositoryMetadata repositoryMetadata, RepositoryFactoryConfiguration configuration,
                                   Example example) {
        final OperatorContext operatorContext = configuration.getDescriptionExtractor().getOperatorContext();
        final Map<String, Object> values = extractValues(example.getProbe());
        final ExampleMatcher matcher = example.getMatcher();
        final List<Parameter> parameters = new ArrayList<>();
        int index = 0;
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            final String propertyPath = entry.getKey();
            if (matcher.isIgnoredPath(propertyPath)) {
                continue;
            }
            final Set<Modifier> modifiers = new HashSet<>();
            final Operator operator;
            if (entry.getValue() == null) {
                if (ExampleMatcher.NullHandler.IGNORE.equals(matcher.getNullHandler())) {
                    continue;
                } else {
                    operator = operatorContext.getBySuffix("IsNull");
                }
            } else {
                if (ignoreCase(matcher, propertyPath)) {
                    modifiers.add(Modifier.IGNORE_CASE);
                }
                final ExampleMatcher.StringMatcher stringMatcher = stringMatcher(matcher, propertyPath);
                if (ExampleMatcher.StringMatcher.STARTING.equals(stringMatcher)) {
                    operator = operatorContext.getBySuffix("StartsWith");
                } else if (ExampleMatcher.StringMatcher.ENDING.equals(stringMatcher)) {
                    operator = operatorContext.getBySuffix("EndsWith");
                } else if (ExampleMatcher.StringMatcher.CONTAINING.equals(stringMatcher)) {
                    operator = operatorContext.getBySuffix("Contains");
                } else if (ExampleMatcher.StringMatcher.REGEX.equals(stringMatcher)) {
                    operator = operatorContext.getBySuffix("Matches");
                } else {
                    operator = operatorContext.getBySuffix("Is");
                }
            }
            parameters.add(new ImmutableParameter(propertyPath, modifiers, new int[]{index++}, operator));
        }
        return new DefaultQueryDescriptor(false, null, 0, null, null, Collections.singletonList(parameters),
                                          configuration, repositoryMetadata);
    }

    private ExampleMatcher.StringMatcher stringMatcher(ExampleMatcher matcher, String path) {
        final ExampleMatcher.PropertySpecifier specifier = matcher.getPropertySpecifiers().getForPath(path);
        return specifier != null ? specifier.getStringMatcher() : matcher.getDefaultStringMatcher();
    }

    private boolean ignoreCase(ExampleMatcher matcher, String path) {
        final ExampleMatcher.PropertySpecifier specifier = matcher.getPropertySpecifiers().getForPath(path);
        return matcher.isIgnoreCaseEnabled() || specifier != null && Boolean.TRUE.equals(specifier.getIgnoreCase());
    }

    /**
     * Given an input object, this method will return a map from the property paths to their corresponding values
     *
     * @param object the input object
     * @return the map of values
     */
    private Map<String, Object> extractValues(Object object) {
        final Map<String, Object> result = new HashMap<>();
        final BeanWrapper wrapper = new BeanWrapperImpl(object);
        for (PropertyDescriptor descriptor : wrapper.getPropertyDescriptors()) {
            if (descriptor.getReadMethod() == null || descriptor.getWriteMethod() == null) {
                continue;
            }
            final String propertyName = descriptor.getName();
            final Object value = PropertyUtils.getPropertyValue(object, propertyName);
            if (value == null) {
                result.put(propertyName, null);
                continue;
            }
            if (isIntractable(descriptor, value)) {
                result.put(propertyName, value);
                continue;
            }
            final Map<String, Object> children = extractValues(value);
            for (Map.Entry<String, Object> entry : children.entrySet()) {
                result.put(propertyName + "." + entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    /**
     * This method is used to determine if a given value should be broken down further or should it be passed in as it
     * is
     *
     * @param descriptor the descriptor for the property
     * @param value      the value for the property
     * @return {@literal true} if the value should be left alone
     */
    private boolean isIntractable(PropertyDescriptor descriptor, Object value) {
        final Class<?> type = descriptor.getPropertyType();
        return type.isPrimitive() || type.getName().startsWith("java.lang.") || value instanceof Iterable
                || value instanceof Map;
    }

}
