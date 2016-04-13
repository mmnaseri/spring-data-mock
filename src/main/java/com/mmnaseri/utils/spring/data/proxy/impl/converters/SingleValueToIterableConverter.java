package com.mmnaseri.utils.spring.data.proxy.impl.converters;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.Collections;
import java.util.Iterator;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/28/15)
 */
public class SingleValueToIterableConverter extends AbstractResultConverter {

    @Override
    protected Object doConvert(Invocation invocation, Object original) {
        if (original instanceof Iterable || original instanceof Iterator) {
            return original;
        }
        return Collections.singletonList(original);
    }

}
