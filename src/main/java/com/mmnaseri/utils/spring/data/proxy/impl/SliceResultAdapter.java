package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
public class SliceResultAdapter extends AbstractIterableResultAdapter<Slice> {

    public SliceResultAdapter() {
        super(-250);
    }

    @Override
    protected Slice doAdapt(Invocation invocation, Iterable iterable) {
        final List content = new ArrayList();
        for (Object item : iterable) {
            //noinspection unchecked
            content.add(item);
        }
        //noinspection unchecked
        return new SliceImpl(content);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return originalValue != null && invocation.getMethod().getReturnType().equals(Slice.class);
    }

}
