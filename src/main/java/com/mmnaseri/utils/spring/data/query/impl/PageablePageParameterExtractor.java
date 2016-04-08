package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.query.Page;
import com.mmnaseri.utils.spring.data.query.PageParameterExtractor;
import org.springframework.data.domain.Pageable;

import java.util.Objects;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
public class PageablePageParameterExtractor implements PageParameterExtractor {

    private final int index;

    public PageablePageParameterExtractor(int index) {
        this.index = index;
    }

    @Override
    public Page extract(Invocation invocation) {
        final Object value = invocation.getArguments()[index];
        if (value == null) {
            throw new InvalidArgumentException("Page value should not be empty");
        }
        if (value instanceof Pageable) {
            final Pageable pageable = (Pageable) value;
            return new ImmutablePage(pageable.getPageSize(), pageable.getPageNumber());
        }
        throw new InvalidArgumentException("No valid value was passed to deduce the paging description from");
    }

}
