package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.sample.mocks.StringifiableDataStoreOperation;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public class ImmutableInvocationMappingTest {

    @Test
    public void testToString() throws Exception {
        final String string = "string representation";
        final Method method = ReturnTypeSampleRepository.class.getMethod("doSomething");
        final ImmutableInvocationMapping<Object, Object> mapping = new ImmutableInvocationMapping<>(method, new StringifiableDataStoreOperation<>(string));
        assertThat(mapping.toString(), is(method.toString() + " -> " + string));
    }

}