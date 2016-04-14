package com.mmnaseri.utils.spring.data.proxy.impl.resolvers;

import com.mmnaseri.utils.spring.data.domain.impl.MethodInvocationDataStoreOperation;
import com.mmnaseri.utils.spring.data.proxy.TypeMapping;
import com.mmnaseri.utils.spring.data.proxy.impl.ImmutableTypeMapping;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.resolvers.*;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public class SignatureDataOperationResolverTest {

    private SignatureDataOperationResolver resolver;

    @BeforeMethod
    public void setUp() throws Exception {
        final List<TypeMapping<?>> mappings = new ArrayList<>();
        mappings.add(new ImmutableTypeMapping<>(SuperInterface.class, new SuperInterfaceImpl()));
        mappings.add(new ImmutableTypeMapping<>(ChildClass.class, new ChildClass()));
        resolver = new SignatureDataOperationResolver(mappings);
    }

    @Test
    public void testLookingForExactMatchViaInterface() throws Exception {
        final DataStoreOperation<?, ?, ?> operation = resolver.resolve(ProxiedClass.class.getMethod("saySomething", String.class, Double.class));
        assertThat(operation, is(notNullValue()));
        assertThat(operation, is(instanceOf(MethodInvocationDataStoreOperation.class)));
        final MethodInvocationDataStoreOperation invocationOperation = (MethodInvocationDataStoreOperation) operation;
        assertThat(invocationOperation.getInstance(), is(instanceOf(SuperInterface.class)));
        assertThat(invocationOperation.getMethod(), is(SuperInterface.class.getMethod("saySomething", CharSequence.class, Double.class)));
    }

    @Test
    public void testLookingForExactMatchViaClass() throws Exception {
        final DataStoreOperation<?, ?, ?> operation = resolver.resolve(ProxiedClass.class.getMethod("saySomething", String.class, Integer.class));
        assertThat(operation, is(notNullValue()));
        assertThat(operation, is(instanceOf(MethodInvocationDataStoreOperation.class)));
        final MethodInvocationDataStoreOperation invocationOperation = (MethodInvocationDataStoreOperation) operation;
        assertThat(invocationOperation.getInstance(), is(instanceOf(ChildClass.class)));
        assertThat(invocationOperation.getMethod(), is(ChildClass.class.getMethod("saySomething", String.class, Integer.class)));
    }

    @Test
    public void testLookingForMatchViaSuperClassSuperSignature() throws Exception {
        final DataStoreOperation<?, ?, ?> operation = resolver.resolve(ProxiedClass.class.getMethod("saySomething", String.class, float.class));
        assertThat(operation, is(notNullValue()));
        assertThat(operation, is(instanceOf(MethodInvocationDataStoreOperation.class)));
        final MethodInvocationDataStoreOperation invocationOperation = (MethodInvocationDataStoreOperation) operation;
        assertThat(invocationOperation.getInstance(), is(instanceOf(SuperClass.class)));
        assertThat(invocationOperation.getMethod(), is(SuperClass.class.getMethod("saySomething", CharSequence.class, Number.class)));
    }

    @Test
    public void testLookingForMaskedMethod() throws Exception {
        final DataStoreOperation<?, ?, ?> operation = resolver.resolve(ProxiedClass.class.getMethod("doSomething"));
        assertThat(operation, is(notNullValue()));
        assertThat(operation, is(instanceOf(MethodInvocationDataStoreOperation.class)));
        final MethodInvocationDataStoreOperation invocationOperation = (MethodInvocationDataStoreOperation) operation;
        assertThat(invocationOperation.getInstance(), is(instanceOf(SuperInterface.class)));
        assertThat(invocationOperation.getMethod(), is(SuperInterface.class.getMethod("doSomething")));
    }

    @Test
    public void testLookingForMethodThatDoesNotMatch() throws Exception {
        final DataStoreOperation<?, ?, ?> resolved = resolver.resolve(ProxiedClass.class.getMethod("saySomething", String.class, Boolean.class));
        assertThat(resolved, is(nullValue()));
    }

}