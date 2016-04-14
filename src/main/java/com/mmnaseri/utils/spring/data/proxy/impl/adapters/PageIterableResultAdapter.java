package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class will adapt results from an iterable object to a page.</p>
 *
 * <p>It will accept adaptations wherein the original value is some sort of iterable and the required return type
 * is an instance of {@link Page}. Remember that it does <em>not</em> check for individual object type
 * compatibility.</p>
 *
 * <p>This adapter will execute at priority {@literal -200}.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/28/15)
 */
public class PageIterableResultAdapter extends AbstractIterableResultAdapter<Page> {

    public PageIterableResultAdapter() {
        super(-200);
    }

    @Override
    protected Page doAdapt(Invocation invocation, Iterable iterable) {
        final List content = new ArrayList();
        for (Object item : iterable) {
            //noinspection unchecked
            content.add(item);
        }
        //noinspection unchecked
        return new PageImpl(content);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return originalValue != null && originalValue instanceof Iterable && invocation.getMethod().getReturnType().equals(Page.class);
    }

}
