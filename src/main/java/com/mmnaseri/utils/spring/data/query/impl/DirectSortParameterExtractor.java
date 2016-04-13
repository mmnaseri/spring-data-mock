package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.query.Sort;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/19/15)
 */
public class DirectSortParameterExtractor extends AbstractSortParameterExtractor {

    private final int index;

    public DirectSortParameterExtractor(int index) {
        this.index = index;
    }

    @Override
    public Sort extract(Invocation invocation) {
        if (invocation == null) {
            throw new InvalidArgumentException("Invocation cannot be null");
        }
        final Object value = invocation.getArguments()[index];
        if (value == null) {
            throw new InvalidArgumentException("Page value should not be empty");
        }
        if (value instanceof org.springframework.data.domain.Sort) {
            return getSort((org.springframework.data.domain.Sort) value);
        }
        throw new InvalidArgumentException("No valid value was passed to deduce the paging description from");
    }
}
