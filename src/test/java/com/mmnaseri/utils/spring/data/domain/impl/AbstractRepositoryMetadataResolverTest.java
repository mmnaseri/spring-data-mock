package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.error.RepositoryDefinitionException;
import com.mmnaseri.utils.spring.data.sample.mocks.NullReturningRepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.sample.models.EmptyEntity;
import org.springframework.data.repository.Repository;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/30/15)
 */
public class AbstractRepositoryMetadataResolverTest {

    @Test(expectedExceptions = RepositoryDefinitionException.class, expectedExceptionsMessageRegExp = ".*?: Repository interface must not be null")
    public void testThatItDoesNotAcceptNull() throws Exception {
        new NullReturningRepositoryMetadataResolver().resolve(null);
    }

    @Test(expectedExceptions = RepositoryDefinitionException.class, expectedExceptionsMessageRegExp = ".*?: Cannot resolve repository metadata for a class object that isn't an interface")
    public void testThatItOnlyAcceptsInterfaces() throws Exception {
        new NullReturningRepositoryMetadataResolver().resolve(EmptyEntity.class);
    }

    @Test(expectedExceptions = RepositoryDefinitionException.class, expectedExceptionsMessageRegExp = ".*?: Repository interface needs to be declared as public")
    public void testThatItOnlyAcceptsPublicInterfaces() throws Exception {
        new NullReturningRepositoryMetadataResolver().resolve(SamplePrivateInterface.class);
    }

    @Test
    public void testThatItPassesAPublicInterfaceToTheSubClass() throws Exception {
        final NullReturningRepositoryMetadataResolver resolver = new NullReturningRepositoryMetadataResolver();
        resolver.resolve(Repository.class);
        assertThat(resolver.getRepositoryInterface(), equalTo((Class) Repository.class));
    }

    private interface SamplePrivateInterface {}

}