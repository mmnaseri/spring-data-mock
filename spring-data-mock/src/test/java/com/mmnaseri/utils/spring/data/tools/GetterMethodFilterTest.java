package com.mmnaseri.utils.spring.data.tools;

import com.mmnaseri.utils.spring.data.sample.models.SampleClass;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/9/16)
 */
public class GetterMethodFilterTest {

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