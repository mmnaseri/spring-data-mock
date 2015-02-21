package com.agileapes.utils.spring.impl.strategies;

import com.agileapes.utils.spring.InterceptionStrategy;
import com.agileapes.utils.spring.RepositoryDescriptor;
import com.agileapes.utils.spring.domain.QueryMethodItemMatcher;
import com.agileapes.utils.spring.domain.QueryMethodMetadata;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 14:00)
 */
public class CountQueryMethodStrategy<E, K extends Serializable, R extends Repository<E, K>> implements InterceptionStrategy {

    private final Map<K, E> data;
    private final RepositoryDescriptor<E, K, R> descriptor;

    public CountQueryMethodStrategy(Map<K, E> data, RepositoryDescriptor<E, K, R> descriptor) {
        this.data = data;
        this.descriptor = descriptor;
    }

    @Override
    public boolean intercepts(Method method) {
        return (Number.class.isAssignableFrom(method.getReturnType()) || long.class.equals(method.getReturnType())
            || int.class.equals(method.getReturnType()) || byte.class.equals(method.getReturnType())
            || short.class.equals(method.getReturnType())) && method.getName().matches("count.*?By.+");
    }

    @Override
    public Object call(Object target, Method method, Object... parameters) throws Throwable {
        final QueryMethodMetadata<E, K, R> metadata = new QueryMethodMetadata<E, K, R>(method, descriptor);
        final QueryMethodItemMatcher<E, K, R> matcher = new QueryMethodItemMatcher<E, K, R>(metadata);
        if (!"count".equals(metadata.getFunction())) {
            throw new IllegalArgumentException("Expected aggregate function `count` to have been used: " + method.getName());
        }
        long count = 0;
        for (E entity : data.values()) {
            if (matcher.matches(entity, parameters)) {
                count ++;
            }
        }
        return count;
    }

}
