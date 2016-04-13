package com.mmnaseri.utils.spring.data.proxy;

import com.mmnaseri.utils.spring.data.domain.Invocation;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/28/15)
 */
public interface ResultConverter {

    Object convert(Invocation invocation, Object original);

}
