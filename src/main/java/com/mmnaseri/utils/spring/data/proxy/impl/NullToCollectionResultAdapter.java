package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.tools.CollectionInstanceUtils;

import java.util.Collection;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/24/15)
 */
public class NullToCollectionResultAdapter extends AbstractResultAdapter<Collection> {

    public NullToCollectionResultAdapter() {
        super(-300);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return originalValue == null && Collection.class.isAssignableFrom(invocation.getMethod().getReturnType());
    }

    @Override
    public Collection adapt(Invocation invocation, Object originalValue) {
        return CollectionInstanceUtils.getCollection(invocation.getMethod().getReturnType());
    }

}
