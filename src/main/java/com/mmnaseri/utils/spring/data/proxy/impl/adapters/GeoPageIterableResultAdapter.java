package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import org.springframework.data.geo.GeoPage;
import org.springframework.data.geo.GeoResults;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/28/15)
 */
public class GeoPageIterableResultAdapter extends AbstractIterableResultAdapter<GeoPage> {

    public GeoPageIterableResultAdapter() {
        super(-150);
    }

    @Override
    protected GeoPage doAdapt(Invocation invocation, Iterable iterable) {
        final List content = new ArrayList();
        for (Object item : iterable) {
            //noinspection unchecked
            content.add(item);
        }
        //noinspection unchecked
        return new GeoPage(new GeoResults(content));
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return originalValue != null && originalValue instanceof Iterable && invocation.getMethod().getReturnType().equals(GeoPage.class);
    }

}
