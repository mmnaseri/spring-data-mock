package com.mmnaseri.utils.spring.data.domain;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public interface OperatorContext {

    void register(Operator operator);

    Operator getBySuffix(String suffix);

}
