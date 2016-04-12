package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.impl.DefaultOperatorContext;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultRepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.query.impl.DefaultDataFunctionRegistry;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreRegistry;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 1:51 PM)
 */
public class ImmutableRepositoryFactoryConfigurationTest {

    @Test
    public void testCopyConstructor() throws Exception {
        final DefaultRepositoryFactoryConfiguration original = new DefaultRepositoryFactoryConfiguration();
        original.setDescriptionExtractor(new QueryDescriptionExtractor(new DefaultOperatorContext()));
        original.setEventListenerContext(new DefaultDataStoreEventListenerContext());
        original.setFunctionRegistry(new DefaultDataFunctionRegistry());
        original.setRepositoryMetadataResolver(new DefaultRepositoryMetadataResolver());
        original.setResultAdapterContext(new DefaultResultAdapterContext());
        original.setTypeMappingContext(new DefaultTypeMappingContext());
        original.setDataStoreRegistry(new DefaultDataStoreRegistry());
        original.setOperationInvocationHandler(new NonDataOperationInvocationHandler());
        final ImmutableRepositoryFactoryConfiguration copy = new ImmutableRepositoryFactoryConfiguration(original);
        assertThat(copy.getTypeMappingContext(), is(original.getTypeMappingContext()));
        assertThat(copy.getResultAdapterContext(), is(original.getResultAdapterContext()));
        assertThat(copy.getRepositoryMetadataResolver(), is(original.getRepositoryMetadataResolver()));
        assertThat(copy.getOperationInvocationHandler(), is(original.getOperationInvocationHandler()));
        assertThat(copy.getFunctionRegistry(), is(original.getFunctionRegistry()));
        assertThat(copy.getEventListenerContext(), is(original.getEventListenerContext()));
        assertThat(copy.getDescriptionExtractor(), is(original.getDescriptionExtractor()));
        assertThat(copy.getDataStoreRegistry(), is(original.getDataStoreRegistry()));
    }
    
}