package com.mmnaseri.utils.spring.data.proxy.impl.resolvers;

import com.mmnaseri.utils.spring.data.domain.impl.DescribedDataStoreOperation;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.resolvers.NoOpQueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.resolvers.SampleMappedRepository;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class QueryMethodDataOperationResolverTest {

    @Test
    public void testThatItCallsAQueryExtractor() throws Exception {
        final NoOpQueryDescriptionExtractor extractor = new NoOpQueryDescriptionExtractor();
        final QueryMethodDataOperationResolver resolver = new QueryMethodDataOperationResolver(extractor, null, null, null);
        assertThat(extractor.isCalled(), is(false));
        final DataStoreOperation<?, ?, ?> operation = resolver.resolve(SampleMappedRepository.class.getMethod("normalMethod"));
        assertThat(operation, is(notNullValue()));
        assertThat(operation, is(instanceOf(DescribedDataStoreOperation.class)));
        assertThat(extractor.isCalled(), is(true));
    }

    @Test
    public void testMethodThatIsAnnotatedWithVendorQuery() throws Exception {
        final QueryMethodDataOperationResolver resolver = new QueryMethodDataOperationResolver(new NoOpQueryDescriptionExtractor(), null, null, null);
        final DataStoreOperation<?, ?, ?> method = resolver.resolve(SampleMappedRepository.class.getMethod("nativeMethod"));
        assertThat(method, is(nullValue()));
    }

}