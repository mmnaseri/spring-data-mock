package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.query.impl.DefaultDataFunctionRegistry;
import com.mmnaseri.utils.spring.data.query.impl.DefaultQueryDescriptor;
import com.mmnaseri.utils.spring.data.sample.mocks.SpyingDataFunction;
import com.mmnaseri.utils.spring.data.sample.mocks.SpyingSelectDataStoreOperation;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class DescribedDataStoreOperationTest {

    @Test
    public void testExecutionWithoutAnyFunctions() {
        final DefaultQueryDescriptor descriptor = new DefaultQueryDescriptor(false, null, 0, null, null, null, null,
                                                                             null);
        final List<Object> selection = new ArrayList<>();
        final SpyingSelectDataStoreOperation<Object, Object> operationSpy = new SpyingSelectDataStoreOperation<>(
                descriptor, selection);
        final DescribedDataStoreOperation<Object, Object> operation = new DescribedDataStoreOperation<>(operationSpy,
                                                                                                        null);
        assertThat(operationSpy.isCalled(), is(false));
        final Object result = operation.execute(new MemoryDataStore<>(Object.class), null, null);
        assertThat(operationSpy.isCalled(), is(true));
        assertThat(result, Matchers.is(selection));
    }

    @Test
    public void testExecutionWithCustomFunction() {
        final DefaultQueryDescriptor descriptor = new DefaultQueryDescriptor(false, "xyz", 0, null, null, null, null,
                                                                             null);
        final List<Object> selection = new ArrayList<>();
        final Object transformed = new Object();
        final DefaultDataFunctionRegistry functionRegistry = new DefaultDataFunctionRegistry();
        final SpyingDataFunction<Object> spy = new SpyingDataFunction<>(new DataFunction<Object>() {

            @Override
            public <K, E> Object apply(DataStore<K, E> dataStore, QueryDescriptor query,
                                       RepositoryConfiguration configuration, List<E> current) {
                return transformed;
            }
        });
        functionRegistry.register("xyz", spy);
        final SpyingSelectDataStoreOperation<Object, Object> operationSpy = new SpyingSelectDataStoreOperation<>(
                descriptor, selection);
        final DescribedDataStoreOperation<Object, Object> operation = new DescribedDataStoreOperation<>(operationSpy,
                                                                                                        functionRegistry);
        assertThat(operationSpy.isCalled(), is(false));
        final Object result = operation.execute(new MemoryDataStore<>(Object.class), null, null);
        assertThat(operationSpy.isCalled(), is(true));
        assertThat(result, is(transformed));
        assertThat(spy.getInvocations(), hasSize(1));
        assertThat(spy.getInvocations().get(0).getSelection(), Matchers.<Object>is(selection));
    }

    @Test
    public void testToString() {
        final SelectDataStoreOperation<Object, Object> selectOperation = new SelectDataStoreOperation<>(
                new DefaultQueryDescriptor(false, null, 0, null, null, null, null, null));
        final DescribedDataStoreOperation<Object, Object> describedOperation = new DescribedDataStoreOperation<>(
                selectOperation, null);
        assertThat(describedOperation.toString(), is(selectOperation.toString()));
    }

}