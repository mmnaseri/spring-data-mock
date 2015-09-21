package com.mmnaseri.utils.spring.data.domain;

import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public interface Invocation {

    Method getMethod();

    Object[] getArguments();

}
