package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import org.springframework.data.domain.Slice;
import org.springframework.data.geo.GeoPage;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;

import java.util.Collections;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
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
        return new GeoPage(new GeoResults(Collections.<GeoResult>emptyList()));
    }

}
