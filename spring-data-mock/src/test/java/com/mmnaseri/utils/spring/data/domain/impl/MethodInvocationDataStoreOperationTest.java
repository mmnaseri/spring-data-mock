package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.error.DataOperationExecutionException;
import com.mmnaseri.utils.spring.data.sample.usecases.domain.MappedClass;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public class MethodInvocationDataStoreOperationTest {

    @Test(expectedExceptions = DataOperationExecutionException.class)
    public void testAccessingPrivateMethod() throws Exception {
        final MethodInvocationDataStoreOperation<Object, Object> operation = new MethodInvocationDataStoreOperation<>(new MappedClass(null), MappedClass.class.getDeclaredMethod("privateMethod"));
        operation.execute(null, null, new ImmutableInvocation(null, new Object[]{}));
    }

    @Test(expectedExceptions = DataOperationExecutionException.class)
    public void testAccessingErrorThrowingMethod() throws Exception {
        final MethodInvocationDataStoreOperation<Object, Object> operation = new MethodInvocationDataStoreOperation<>(new MappedClass(null), MappedClass.class.getDeclaredMethod("errorThrowingMethod"));
        operation.execute(null, null, new ImmutableInvocation(null, new Object[]{}));
    }

    @Test
    public void testCallingNormalMethod() throws Exception {
        final Object value = new Object();
        final MethodInvocationDataStoreOperation<Object, Object> operation = new MethodInvocationDataStoreOperation<>(new MappedClass(value), MappedClass.class.getDeclaredMethod("validMethod"));
        final Object result = operation.execute(null, null, new ImmutableInvocation(null, new Object[]{}));
        assertThat(result, is(value));
    }

    @Test
    public void testToString() throws Exception {
        final Method method = MappedClass.class.getDeclaredMethod("errorThrowingMethod");
        final MappedClass instance = new MappedClass(null);
        final MethodInvocationDataStoreOperation<Object, Object> operation = new MethodInvocationDataStoreOperation<>(instance, method);
        assertThat(operation.toString(), is(method + " on " + instance));
    }

}