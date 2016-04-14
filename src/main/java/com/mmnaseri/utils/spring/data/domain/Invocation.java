package com.mmnaseri.utils.spring.data.domain;

import java.lang.reflect.Method;

/**
 * This interface encapsulates a single invocation, regardless of the object on which the method was invoked.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/17/15)
 */
public interface Invocation {

    /**
     * @return the method that was invoked
     */
    Method getMethod();

    /**
     * @return the arguments with which that method was invoked
     */
    Object[] getArguments();

}
