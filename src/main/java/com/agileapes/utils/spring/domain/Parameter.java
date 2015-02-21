package com.agileapes.utils.spring.domain;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 2:39)
 */
public class Parameter {

    private String name;
    private Operator operator;
    private int[] indices;
    private boolean ignoreCase;

    public Parameter(String name, Operator operator, int[] indices, boolean ignoreCase) {
        this.name = name;
        this.operator = operator;
        this.indices = indices;
        this.ignoreCase = ignoreCase;
    }

    public String getName() {
        return name;
    }

    public Operator getOperator() {
        return operator;
    }

    public int[] getIndices() {
        return indices;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

}

