package com.agileapes.utils.spring.domain;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 2:40)
 */
public interface Matcher {

    boolean matches(Parameter parameter, Object value, Object... parameters);

}
