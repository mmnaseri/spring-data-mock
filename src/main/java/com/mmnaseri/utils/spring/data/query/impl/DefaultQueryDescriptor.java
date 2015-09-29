package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.domain.Operator;
import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.query.*;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;

import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/20/15)
 */
public class DefaultQueryDescriptor implements QueryDescriptor {
    
    private final boolean distinct;
    private final String function;
    private final int limit;
    private final PageParameterExtractor pageExtractor;
    private final SortParameterExtractor sortExtractor;
    private final List<List<Parameter>> branches;
    private final RepositoryMetadata repositoryMetadata;

    public DefaultQueryDescriptor(boolean distinct, String function, int limit, PageParameterExtractor pageExtractor, SortParameterExtractor sortExtractor, List<List<Parameter>> branches, RepositoryMetadata repositoryMetadata) {
        this.distinct = distinct;
        this.function = function;
        this.limit = limit;
        this.pageExtractor = pageExtractor;
        this.sortExtractor = sortExtractor;
        this.branches = branches;
        this.repositoryMetadata = repositoryMetadata;
    }

    @Override
    public RepositoryMetadata getRepositoryMetadata() {
        return repositoryMetadata;
    }

    @Override
    public boolean isDistinct() {
        return distinct;
    }

    @Override
    public String getFunction() {
        return function;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public Page getPage(Invocation invocation) {
        return pageExtractor == null ? null : pageExtractor.extract(invocation);
    }

    @Override
    public Sort getSort(Invocation invocation) {
        return sortExtractor == null ? null : sortExtractor.extract(invocation);
    }

    @Override
    public boolean matches(Object entity, Invocation invocation) {
        for (List<Parameter> branch : branches) {
            boolean matches = true;
            for (Parameter parameter : branch) {
                final Object value = PropertyUtils.getPropertyValue(entity, parameter.getPath());
                final Operator operator = parameter.getOperator();
                final Object[] properties = new Object[operator.getOperands()];
                for (int i = 0; i < operator.getOperands(); i++) {
                    properties[i] = invocation.getArguments()[parameter.getIndices()[i]];
                }
                if (!operator.getMatcher().matches(parameter, value, properties)) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                return true;
            }
        }
        return false;
    }
    
}