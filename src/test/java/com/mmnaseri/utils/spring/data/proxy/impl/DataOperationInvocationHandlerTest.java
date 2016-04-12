package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableRepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.key.UUIDKeyGenerator;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import com.mmnaseri.utils.spring.data.domain.repository.SimplePersonRepository;
import com.mmnaseri.utils.spring.data.proxy.InvocationMapping;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import org.hamcrest.Matchers;
import org.springframework.data.repository.Repository;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DataOperationInvocationHandlerTest {

    private interface Sample extends Repository<Person, String> {

        Object doSomething();

    }

    private static class SpyingOperation implements DataStoreOperation<Object, String, Person> {

        private DataStore<String, Person> store;

        private RepositoryConfiguration configuration;
        private Invocation invocation;
        private final Object result;

        private SpyingOperation(Object result) {
            this.result = result;
        }

        @Override
        public Object execute(DataStore<String, Person> store, RepositoryConfiguration configuration, Invocation invocation) {
            this.store = store;
            this.configuration = configuration;
            this.invocation = invocation;
            return result;
        }

        public DataStore<String, Person> getStore() {
            return store;
        }

        public RepositoryConfiguration getConfiguration() {
            return configuration;
        }

        public Invocation getInvocation() {
            return invocation;
        }

    }

    private DataOperationInvocationHandler<String, Person> handler;
    private List<InvocationMapping<String, Person>> mappings;

    @BeforeMethod
    public void setUp() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, Person.class, SimplePersonRepository.class, "id");
        final RepositoryConfiguration repositoryConfiguration = new ImmutableRepositoryConfiguration(repositoryMetadata, new UUIDKeyGenerator(), Collections.<Class<?>>emptyList());
        final MemoryDataStore<String, Person> dataStore = new MemoryDataStore<>(Person.class);
        final DefaultResultAdapterContext adapterContext = new DefaultResultAdapterContext();
        final NonDataOperationInvocationHandler invocationHandler = new NonDataOperationInvocationHandler();
        mappings = new ArrayList<>();
        handler = new DataOperationInvocationHandler<>(repositoryConfiguration, mappings, dataStore, adapterContext, invocationHandler);
    }

    /**
     * Regression test to reproduce #12
     * @throws Throwable
     */
    @Test
    public void testCallingHashCode() throws Throwable {
        final Object proxy = new Object();
        final Object result = handler.invoke(proxy, Object.class.getMethod("hashCode"), new Object[]{});
        assertThat(result, is(notNullValue()));
        assertThat(result, Matchers.<Object>is(proxy.hashCode()));
    }

    /**
     * Regression test to reproduce #12
     * @throws Throwable
     */
    @Test
    public void testCallingEqualsWhenTheyAreIdentical() throws Throwable {
        final Object proxy = new Object();
        final Object result = handler.invoke(proxy, Object.class.getMethod("equals", Object.class), new Object[]{proxy});
        assertThat(result, is(notNullValue()));
        assertThat(result, Matchers.<Object>is(true));
    }

    /**
     * Regression test to reproduce #12
     * @throws Throwable
     */
    @Test
    public void testCallingEqualsWhenTheyAreNotIdentical() throws Throwable {
        final Object proxy = new Object();
        final Object result = handler.invoke(proxy, Object.class.getMethod("equals", Object.class), new Object[]{new Object()});
        assertThat(result, is(notNullValue()));
        assertThat(result, Matchers.<Object>is(false));
    }

    /**
     * Regression test to reproduce #12
     * @throws Throwable
     */
    @Test
    public void testCallingToString() throws Throwable {
        final Object proxy = new Object();
        final Object result = handler.invoke(proxy, Object.class.getMethod("toString"), new Object[]{});
        assertThat(result, is(notNullValue()));
        assertThat(result, Matchers.<Object>is(proxy.toString()));
    }

    @Test
    public void testMissingMethodTwice() throws Throwable {
        assertThat(handler.invoke(new Object(), Object.class.getMethod("toString"), new Object[]{}), is(notNullValue()));
        assertThat(handler.invoke(new Object(), Object.class.getMethod("toString"), new Object[]{}), is(notNullValue()));
    }

    @Test
    public void testInvokingBoundMapping() throws Throwable {
        final Object originalValue = new Object();
        final SpyingOperation spy = new SpyingOperation(originalValue);
        mappings.add(new ImmutableInvocationMapping<>(Sample.class.getMethod("doSomething"), spy));
        final Object[] args = {1, 2, 3};
        final Object result = handler.invoke(new Object(), Sample.class.getMethod("doSomething"), args);
        assertThat(spy.getInvocation(), is(notNullValue()));
        assertThat(spy.getInvocation().getMethod(), is(Sample.class.getMethod("doSomething")));
        assertThat(spy.getInvocation().getArguments(), is(args));
        assertThat(result, is(originalValue));
    }

    @Test
    public void testInvokingBoundMappingTwice() throws Throwable {
        final Object originalValue = new Object();
        final SpyingOperation spy = new SpyingOperation(originalValue);
        final SpyingOperation otherSpy = new SpyingOperation(originalValue);
        mappings.add(new ImmutableInvocationMapping<>(Object.class.getMethod("hashCode"), otherSpy));
        mappings.add(new ImmutableInvocationMapping<>(Sample.class.getMethod("doSomething"), spy));
        assertThat(handler.invoke(new Object(), Sample.class.getMethod("doSomething"), new Object[]{1, 2, 3}), is(originalValue));
        assertThat(handler.invoke(new Object(), Sample.class.getMethod("doSomething"), new Object[]{4, 5, 6}), is(originalValue));
        assertThat(otherSpy.getInvocation(), is(nullValue()));
    }

}
