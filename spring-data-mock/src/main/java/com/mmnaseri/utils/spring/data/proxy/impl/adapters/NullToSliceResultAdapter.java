package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import org.springframework.data.domain.Slice;
import org.springframework.data.geo.GeoPage;
import org.springframework.data.geo.GeoResults;

import java.util.Collections;

/**
 * <p>This adapter will try to adapt a {@literal null} value to a slice.</p>
 *
 * <p>It adapts results if the return type is of type {@link Slice} and the original value is {@literal null}.</p>
 *
 * <p>This adapter runs at the priority of {@literal -200}.</p>
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/24/15)
 */
public class NullToSliceResultAdapter extends AbstractResultAdapter<Slice> {

    public NullToSliceResultAdapter() {
        super(-200);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return originalValue == null && Slice.class.isAssignableFrom(invocation.getMethod().getReturnType());
    }

    @Override
    public Slice adapt(Invocation invocation, Object originalValue) {
        //noinspection unchecked
        return new GeoPage(new GeoResults(Collections.emptyList()));
    }

}
