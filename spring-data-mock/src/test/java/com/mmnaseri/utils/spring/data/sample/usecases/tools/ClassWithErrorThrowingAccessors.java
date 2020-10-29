package com.mmnaseri.utils.spring.data.sample.usecases.tools;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 7:56 PM)
 */
@SuppressWarnings("unused")
public class ClassWithErrorThrowingAccessors {

    public String getId() {
        throw new RuntimeException();
    }

    public void setId(String id) {
        throw new RuntimeException();
    }

}
