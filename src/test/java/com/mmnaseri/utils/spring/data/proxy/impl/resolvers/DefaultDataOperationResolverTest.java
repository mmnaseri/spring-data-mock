package com.mmnaseri.utils.spring.data.proxy.impl.resolvers;

import com.mmnaseri.utils.spring.data.domain.impl.*;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import com.mmnaseri.utils.spring.data.error.UnknownDataOperationException;
import com.mmnaseri.utils.spring.data.proxy.TypeMapping;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultRepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.impl.ImmutableTypeMapping;
import com.mmnaseri.utils.spring.data.query.impl.DefaultDataFunctionRegistry;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import org.springframework.data.jpa.repository.Query;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class DefaultDataOperationResolverTest {

    private DefaultDataOperationResolver resolver;

    @BeforeMethod
    public void setUp() throws Exception {
        final ArrayList<TypeMapping<?>> mappings = new ArrayList<>();
        mappings.add(new ImmutableTypeMapping<>(MappedType.class, new MappedType()));
        final QueryDescriptionExtractor descriptionExtractor = new QueryDescriptionExtractor(new DefaultOperatorContext());
        final ImmutableRepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, Person.class, Sample.class, "id");
        final DefaultDataFunctionRegistry functionRegistry = new DefaultDataFunctionRegistry();
        resolver = new DefaultDataOperationResolver(mappings, descriptionExtractor, repositoryMetadata, functionRegistry, new DefaultRepositoryFactoryConfiguration());
    }

    @Test
    public void testLookingForMethodThatHasMapping() throws Exception {
        final DataStoreOperation<?, ?, ?> operation = resolver.resolve(Sample.class.getMethod("mappedSignature", String.class));
        assertThat(operation, is(notNullValue()));
        assertThat(operation,  is(instanceOf(MethodInvocationDataStoreOperation.class)));
        final MethodInvocationDataStoreOperation invocation = (MethodInvocationDataStoreOperation) operation;
        assertThat(invocation.getInstance(), is(instanceOf(MappedType.class)));
        assertThat(invocation.getMethod(), is(MappedType.class.getMethod("mappedSignature", CharSequence.class)));
    }

    @Test
    public void testLookingForQueryMethod() throws Exception {
        final DataStoreOperation<?, ?, ?> operation = resolver.resolve(Sample.class.getMethod("findByFirstName", String.class));
        assertThat(operation, is(notNullValue()));
        assertThat(operation, is(instanceOf(DescribedDataStoreOperation.class)));
    }

    @Test(expectedExceptions = UnknownDataOperationException.class)
    public void testLookingForUnmappedMethod() throws Exception {
        resolver.resolve(Sample.class.getMethod("nativeMethod"));
    }

    private interface Sample {

        void mappedSignature(String string);

        void findByFirstName(String firstName);

        @Query
        void nativeMethod();

    }

    private static class MappedType {

        public void mappedSignature(CharSequence value) {
        }

    }
}