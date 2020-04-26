package com.mmnaseri.utils.spring.data.sample.usecases.domain;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 7:52 PM)
 */
public class MappedClass {

    private final Object value;

    public MappedClass(Object value) {
        this.value = value;
    }

    private void privateMethod() {
    }

    public void errorThrowingMethod() {
        throw new RuntimeException();
    }

    public Object validMethod() {
        return value;
    }

}
