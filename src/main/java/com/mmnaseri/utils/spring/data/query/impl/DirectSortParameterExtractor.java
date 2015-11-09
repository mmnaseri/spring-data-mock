package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.query.Sort;

import java.util.Objects;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
public class DirectSortParameterExtractor extends AbstractSortParameterExtractor {

    private final int index;

    public DirectSortParameterExtractor(int index) {
        this.index = index;
    }

    @Override
    public Sort extract(Invocation invocation) {
        final Object value = invocation.getArguments()[index];
        Objects.requireNonNull(value, "Page value should not be empty");
        if (value instanceof org.springframework.data.domain.Sort) {
            return getSort((org.springframework.data.domain.Sort) value);
        }
        throw new IllegalArgumentException();
    }
}
