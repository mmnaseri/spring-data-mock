package com.mmnaseri.utils.spring.data.proxy.impl.converters;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.Collections;
import java.util.Iterator;

/**
 * This value will create a {@link Collections#singletonList singleton list} out of the passed value, so long as it is
 * not an iterable or iterator object.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
@SuppressWarnings("WeakerAccess")
public class SingleValueToIterableConverter extends AbstractResultConverter {

    @Override
    protected Object doConvert(Invocation invocation, Object original) {
        if (original instanceof Iterable || original instanceof Iterator) {
            return original;
        }
        return Collections.singletonList(original);
    }

}
