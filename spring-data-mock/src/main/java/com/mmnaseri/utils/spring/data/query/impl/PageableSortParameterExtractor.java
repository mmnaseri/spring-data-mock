package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.query.Sort;
import org.springframework.data.domain.Pageable;

/**
 * This extractor expects to see a {@link Pageable} passed as the last argument to a query method call and tries to ask
 * the pageable for the sort metadata.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
public class PageableSortParameterExtractor extends AbstractSortParameterExtractor {

    private final int index;

    public PageableSortParameterExtractor(int index) {
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
        if (value instanceof Pageable) {
            final Pageable pageable = (Pageable) value;
            final org.springframework.data.domain.Sort sort = pageable.getSort();
            return getSort(sort);
        }
        throw new InvalidArgumentException("No valid value was passed to deduce the sort description from");
    }

}
