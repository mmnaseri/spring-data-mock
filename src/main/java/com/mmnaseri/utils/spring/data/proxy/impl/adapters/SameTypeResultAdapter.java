package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;

/**
 * <p>This adapter will accept and adapt results when the request result type is of the same type or a super
 * type of the available value.</p>
 *
 * <p>This adapter runs at priority {@literal -500}.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/24/15)
 */
public class SameTypeResultAdapter extends AbstractResultAdapter<Object> {

    public SameTypeResultAdapter() {
        super(-500);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return originalValue != null && invocation.getMethod().getReturnType().isInstance(originalValue);
    }

    @Override
    public Object adapt(Invocation invocation, Object originalValue) {
        return originalValue;
    }

}
