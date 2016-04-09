package com.mmnaseri.utils.spring.data.tools;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/9/16)
 */
public class GetterMethodFilterTest {

    private static class SampleClass {

        //returns void
        private void getProperty() {}

        //returns void, has a parameter
        private void getProperty(int parameter) {}

        //returns void, has a parameter, does not start with get
        private void hasProperty() {}

        //has a parameter
        private Object getProperty(String parameter) {
            return null;
        }

        //has a parameter, does not start with get
        private Object hasProperty(String parameter) {
            return null;
        }

        //returns void, does not start with get
        private void hasState() {
        }

        //does not have get as a single word at the beginning
        private String getterMethod() {
            return null;
        }

        //proper getter
        private String getValue() {
            return null;
        }

    }

    @Test
    public void testFailures() throws Exception {
        final GetterMethodFilter filter = new GetterMethodFilter();
        assertThat(filter.matches(SampleClass.class.getDeclaredMethod("getProperty")), is(false));
        assertThat(filter.matches(SampleClass.class.getDeclaredMethod("getProperty", int.class)), is(false));
        assertThat(filter.matches(SampleClass.class.getDeclaredMethod("hasProperty")), is(false));
        assertThat(filter.matches(SampleClass.class.getDeclaredMethod("getProperty", String.class)), is(false));
        assertThat(filter.matches(SampleClass.class.getDeclaredMethod("hasProperty", String.class)), is(false));
        assertThat(filter.matches(SampleClass.class.getDeclaredMethod("hasState")), is(false));
        assertThat(filter.matches(SampleClass.class.getDeclaredMethod("getterMethod")), is(false));
    }

    @Test
    public void testProperGetter() throws Exception {
        final GetterMethodFilter filter = new GetterMethodFilter();
        assertThat(filter.matches(SampleClass.class.getDeclaredMethod("getValue")), is(true));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMethodIsNull() throws Exception {
        final GetterMethodFilter filter = new GetterMethodFilter();
        filter.matches(null);
    }

}