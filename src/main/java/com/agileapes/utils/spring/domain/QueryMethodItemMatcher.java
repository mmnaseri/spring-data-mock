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

package com.agileapes.utils.spring.domain;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 2:36)
 */
public class QueryMethodItemMatcher<E, K extends Serializable, R extends Repository<E, K>> {

    private final QueryMethodMetadata<E, K, R> metadata;

    public QueryMethodItemMatcher(QueryMethodMetadata<E, K, R> metadata) {
        this.metadata = metadata;
    }

    public boolean matches(E entity, Object... parameters) {
        final BeanWrapper wrapper = new BeanWrapperImpl(entity);
        final List<Object> parameterList = Arrays.asList(parameters);
        for (List<Parameter> branch : metadata.getBranches()) {
            boolean matched = true;
            for (Parameter parameter : branch) {
                final Object[] matcherParameters;
                if (parameter.getOperator().getOperands() > 0) {
                    matcherParameters = parameterList.subList(parameter.getIndices()[0], parameter.getIndices()[parameter.getIndices().length - 1] + 1).toArray();
                } else {
                    matcherParameters = new Object[0];
                }
                if (!parameter.getOperator().matches(parameter, wrapper.getPropertyValue(parameter.getName()), matcherParameters)) {
                    matched = false;
                    break;
                }
            }
            if (matched) {
                return true;
            }
        }
        return false;
    }

}
