package com.mmnaseri.utils.spring.data.domain;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public interface InvocationMatcher {

    boolean matches(Object entity, Invocation invocation);

}
