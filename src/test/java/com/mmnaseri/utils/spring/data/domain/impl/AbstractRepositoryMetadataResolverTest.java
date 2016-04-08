package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.error.RepositoryDefinitionException;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class AbstractRepositoryMetadataResolverTest {

    public class NullReturningRepositoryMetadataResolver extends AbstractRepositoryMetadataResolver {

        private Class<?> repositoryInterface;

        @Override
        protected RepositoryMetadata resolveFromInterface(Class<?> repositoryInterface) {
            this.repositoryInterface = repositoryInterface;
            return null;
        }

        public Class<?> getRepositoryInterface() {
            return repositoryInterface;
        }

    }

    @Test(expectedExceptions = RepositoryDefinitionException.class, expectedExceptionsMessageRegExp = ".*?: Repository interface must not be null")
    public void testThatItDoesNotAcceptNull() throws Exception {
        new NullReturningRepositoryMetadataResolver().resolve(null);
    }

    @Test(expectedExceptions = RepositoryDefinitionException.class, expectedExceptionsMessageRegExp = ".*?: Cannot resolve repository metadata for a class object that isn't an interface")
    public void testThatItOnlyAcceptsInterfaces() throws Exception {
        new NullReturningRepositoryMetadataResolver().resolve(SampleClass.class);
    }

    @Test(expectedExceptions = RepositoryDefinitionException.class, expectedExceptionsMessageRegExp = ".*?: Repository interface needs to be declared as public")
    public void testThatItOnlyAcceptsPublicInterfaces() throws Exception {
        new NullReturningRepositoryMetadataResolver().resolve(SamplePrivateInterface.class);
    }

    @Test
    public void testThatItPassesAPublicInterfaceToTheSubClass() throws Exception {
        final NullReturningRepositoryMetadataResolver resolver = new NullReturningRepositoryMetadataResolver();
        resolver.resolve(SamplePublicInterface.class);
        assertThat(resolver.getRepositoryInterface(), equalTo((Class) SamplePublicInterface.class));
    }

    public static class SampleClass {}

    private interface SamplePrivateInterface {}

    public interface SamplePublicInterface {}

}