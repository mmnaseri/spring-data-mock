package com.mmnaseri.utils.spring.data.sample.models;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 5:18 PM)
 */
@SuppressWarnings({"unused", "EmptyMethod"})
public class SampleClass {

    //returns void
    private void getProperty() {
    }

    //returns void, has a parameter
    private void getProperty(int parameter) {
    }

    //returns void, has a parameter, does not start with get
    private void hasProperty() {
    }

    //has a parameter
    @SuppressWarnings("SameReturnValue")
    private Object getProperty(String parameter) {
        return null;
    }

    //has a parameter, does not start with get
    @SuppressWarnings("SameReturnValue")
    private Object hasProperty(String parameter) {
        return null;
    }

    //returns void, does not start with get
    private void hasState() {
    }

    //does not have get as a single word at the beginning
    @SuppressWarnings("SameReturnValue")
    private String getterMethod() {
        return null;
    }

    //proper getter
    @SuppressWarnings("SameReturnValue")
    private String getValue() {
        return null;
    }

}
