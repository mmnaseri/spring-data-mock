package com.mmnaseri.utils.spring.data.proxy.impl.resolvers;

import com.mmnaseri.utils.spring.data.domain.impl.MethodInvocationDataStoreOperation;
import com.mmnaseri.utils.spring.data.proxy.TypeMapping;
import com.mmnaseri.utils.spring.data.proxy.impl.ImmutableTypeMapping;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class SignatureDataOperationResolverTest {

    private SignatureDataOperationResolver resolver;
    private ArrayList<TypeMapping<?>> mappings;

    @BeforeMethod
    public void setUp() throws Exception {
        mappings = new ArrayList<>();
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

    private interface SuperInterface {

        void saySomething(CharSequence sequence, Double number);

        void doSomething();

    }

    private static class SuperInterfaceImpl implements SuperInterface {

        @Override
        public void saySomething(CharSequence sequence, Double number) {

        }

        @Override
        public void doSomething() {

        }
    }

    private static class SuperClass {

        public void saySomething(CharSequence sequence, Number number) {

        }

    }

    private static class ChildClass extends SuperClass {

        public void saySomething(String string, Integer number) {

        }

        public void doSomething() {

        }

    }

    private interface ProxiedClass {

        void saySomething(String something, Double number);

        void saySomething(String something, float number);

        void saySomething(String something, Integer number);

        void saySomething(String something, Boolean flag);

        void doSomething();

    }

}