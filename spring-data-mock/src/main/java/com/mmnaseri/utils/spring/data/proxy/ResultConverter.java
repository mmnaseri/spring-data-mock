package com.mmnaseri.utils.spring.data.proxy;

import com.mmnaseri.utils.spring.data.domain.Invocation;

/**
 * <p>This interface encapsulates the process of converting a result from one type to the other.</p>
 *
 * <p>This is different from adapting results since conversion doesn't involve mandatory operations and
 * can go through without any change to the original value. Also, converters are chained, so that the result
 *  of one is passed to the next and so one, whereas with adapters, you have a single adapter suitable for
 *  the current situation that operates on the result.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/28/15)
 */
public interface ResultConverter {

    /**
     * Called when we need to convert the result
     * @param invocation    the invocation for which the conversion is happening
     * @param original      the original value
     * @return the converted value (or the original value if no conversion happened)
     */
    Object convert(Invocation invocation, Object original);

}
