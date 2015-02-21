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
