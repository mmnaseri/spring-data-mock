package com.mmnaseri.utils.spring.data.domain;

import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public interface Parameter {

    String getPath();

    Set<Modifier> getModifiers();

    int[] getIndices();

    Operator getOperator();

}
