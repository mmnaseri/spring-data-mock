package com.mmnaseri.utils.spring.data.proxy.impl.resolvers;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.DescribedDataStoreOperation;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import org.springframework.data.jpa.repository.Query;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class QueryMethodDataOperationResolverTest {

    private static class NoOpQueryDescriptionExtractor extends QueryDescriptionExtractor {

        private boolean called;

        public NoOpQueryDescriptionExtractor() {
            super(null);
            called = false;
        }

        @Override
        public QueryDescriptor extract(RepositoryMetadata repositoryMetadata, Method method, RepositoryFactoryConfiguration configuration) {
            called = true;
            return null;
        }

        public boolean isCalled() {
            return called;
        }
    }

    @Test
    public void testThatItCallsAQueryExtractor() throws Exception {
        final NoOpQueryDescriptionExtractor extractor = new NoOpQueryDescriptionExtractor();
        final QueryMethodDataOperationResolver resolver = new QueryMethodDataOperationResolver(extractor, null, null, null);
        assertThat(extractor.isCalled(), is(false));
        final DataStoreOperation<?, ?, ?> operation = resolver.resolve(Sample.class.getMethod("normalMethod"));
        assertThat(operation, is(notNullValue()));
        assertThat(operation, is(instanceOf(DescribedDataStoreOperation.class)));
        assertThat(extractor.isCalled(), is(true));
    }

    @Test
    public void testMethodThatIsAnnotatedWithVendorQuery() throws Exception {
        final QueryMethodDataOperationResolver resolver = new QueryMethodDataOperationResolver(new NoOpQueryDescriptionExtractor(), null, null, null);
        final DataStoreOperation<?, ?, ?> method = resolver.resolve(Sample.class.getMethod("method"));
        assertThat(method, is(nullValue()));
    }

    private interface Sample {

        void normalMethod();

        @Query
        void method();

    }

}