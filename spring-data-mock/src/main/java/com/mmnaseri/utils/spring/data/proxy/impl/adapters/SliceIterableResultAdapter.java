package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class will adapt results from an iterable object to a slice.</p>
 *
 * <p>It will accept adaptations wherein the original value is some sort of iterable and the required return type
 * is an instance of {@link Slice}. Remember that it does <em>not</em> check for individual object type
 * compatibility.</p>
 *
 * <p>This adapter will execute at priority {@literal -250}.</p>
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
public class SliceIterableResultAdapter extends AbstractIterableResultAdapter<Slice> {

    public SliceIterableResultAdapter() {
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
        return originalValue instanceof Iterable && invocation.getMethod().getReturnType()
                                                              .equals(Slice.class);
    }

}
