package com.mmnaseri.utils.spring.data.tools;

import org.testng.annotations.Test;

import java.lang.reflect.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public abstract class AbstractUtilityClassTest {

    protected abstract Class<?> getUtilityClass();

    @Test
    public void testConstructorIsPrivate() {
        final Constructor<?>[] constructors = getUtilityClass().getDeclaredConstructors();
        assertThat(constructors, is(arrayWithSize(1)));
        final Constructor<?> constructor = constructors[0];
        assertThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
    }

    @Test
    public void testConstructorThrowsException() throws Exception {
        final Constructor<?>[] constructors = getUtilityClass().getDeclaredConstructors();
        final Constructor<?> constructor = constructors[0];
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (InvocationTargetException e) {
            assertThat(e.getCause(), is(notNullValue()));
            assertThat(e.getCause(), is(instanceOf(UnsupportedOperationException.class)));
            return;
        }
        fail("Expected utility class constructor to throw an exception");
    }

    @Test
    public void testClassIsFinal() {
        assertThat(Modifier.isFinal(getUtilityClass().getModifiers()), is(true));
    }

    @Test
    public void testClassHasNoInstanceMethods() {
        for (Method method : getUtilityClass().getDeclaredMethods()) {
            assertThat(Modifier.isStatic(method.getModifiers()), is(true));
        }
    }

    @Test
    public void testClassHasNoInstanceFields() {
        for (Field field : getUtilityClass().getDeclaredFields()) {
            assertThat(Modifier.isStatic(field.getModifiers()), is(true));
        }
    }

    @Test
    public void testClassHasNoSuperClass() {
        assertThat(getUtilityClass().getSuperclass(), is(equalTo((Class) Object.class)));
    }

}
