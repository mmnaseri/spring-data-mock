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

package com.mmnaseri.utils.spring.impl.strategies;

import com.mmnaseri.utils.spring.InterceptionStrategy;
import com.mmnaseri.utils.spring.RepositoryDescriptor;
import com.mmnaseri.utils.spring.domain.QueryMethodItemMatcher;
import com.mmnaseri.utils.spring.domain.QueryMethodMetadata;
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
