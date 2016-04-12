package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class ImmutableInvocationMappingTest {

    @Test
    public void testToString() throws Exception {
        final String string = "string representation";
        final Method method = Sample.class.getMethod("doSomething");
        final ImmutableInvocationMapping<Serializable, Object> mapping = new ImmutableInvocationMapping<>(method, new StringifiableDataStoreOperation<>(string));
        assertThat(mapping.toString(), is(method.toString() + " -> " + string));
    }

    private interface Sample {

        void doSomething();

    }

    private static class StringifiableDataStoreOperation<R, K extends Serializable, E> implements DataStoreOperation<R, K, E> {

        private final String string;

        private StringifiableDataStoreOperation(String string) {
            this.string = string;
        }

        @Override
        public R execute(DataStore store, RepositoryConfiguration configuration, Invocation invocation) {
            return null;
        }

        @Override
        public String toString() {
            return string;
        }
    }

}