package com.mmnaseri.utils.spring.data.store.impl;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/9/16)
 */
public class PropertyVisitorTest {

    private static class SampleClass {

        @Id
        private Object field;

        @Id
        private void getProperty() {
        }

    }

    @Test
    public void testLookingForFieldWithAnnotation() throws Exception {
        final PropertyVisitor visitor = new PropertyVisitor(Id.class);
        assertThat(visitor.getProperty(), is(nullValue()));
        visitor.doWith(SampleClass.class.getDeclaredField("field"));
        assertThat(visitor.getProperty(), is("field"));
    }

    @Test
    public void testLookingForFieldWithWrongAnnotation() throws Exception {
        final PropertyVisitor visitor = new PropertyVisitor(LastModifiedBy.class);
        assertThat(visitor.getProperty(), is(nullValue()));
        visitor.doWith(SampleClass.class.getDeclaredField("field"));
        assertThat(visitor.getProperty(), is(nullValue()));
    }


    @Test
    public void testLookingForMethodWithAnnotation() throws Exception {
        final PropertyVisitor visitor = new PropertyVisitor(Id.class);
        assertThat(visitor.getProperty(), is(nullValue()));
        visitor.doWith(SampleClass.class.getDeclaredMethod("getProperty"));
        assertThat(visitor.getProperty(), is("property"));
    }

    @Test
    public void testLookingForMethodWithWrongAnnotation() throws Exception {
        final PropertyVisitor visitor = new PropertyVisitor(LastModifiedBy.class);
        assertThat(visitor.getProperty(), is(nullValue()));
        visitor.doWith(SampleClass.class.getDeclaredMethod("getProperty"));
        assertThat(visitor.getProperty(), is(nullValue()));
    }

    @Test
    public void testLookingFieldFirst() throws Exception {
        final PropertyVisitor visitor = new PropertyVisitor(Id.class);
        assertThat(visitor.getProperty(), is(nullValue()));
        visitor.doWith(SampleClass.class.getDeclaredField("field"));
        assertThat(visitor.getProperty(), is("field"));
        visitor.doWith(SampleClass.class.getDeclaredMethod("getProperty"));
        assertThat(visitor.getProperty(), is("field"));
    }

    @Test
    public void testLookingMethodFirst() throws Exception {
        final PropertyVisitor visitor = new PropertyVisitor(Id.class);
        assertThat(visitor.getProperty(), is(nullValue()));
        visitor.doWith(SampleClass.class.getDeclaredMethod("getProperty"));
        assertThat(visitor.getProperty(), is("property"));
        visitor.doWith(SampleClass.class.getDeclaredField("field"));
        assertThat(visitor.getProperty(), is("property"));
    }

}