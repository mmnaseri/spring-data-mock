package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.error.RepositoryDefinitionException;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.sample.repositories.MalformedRepository;
import com.mmnaseri.utils.spring.data.sample.repositories.SimplePersonRepository;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class AssignableRepositoryMetadataResolverTest {

    @Test(expectedExceptions = RepositoryDefinitionException.class,
          expectedExceptionsMessageRegExp = ".*?: Expected interface to extend .*?\\.Repository")
    public void testResolvingFromNonInheritingRepository() throws Exception {
        new AssignableRepositoryMetadataResolver().resolve(MalformedRepository.class);
    }

    @Test
    public void testResolvingFromInheritingRepository() throws Exception {
        final RepositoryMetadata metadata = new AssignableRepositoryMetadataResolver().resolve(
                SimplePersonRepository.class);
        assertThat(metadata, is(notNullValue()));
        assertThat(metadata.getRepositoryInterface(), equalTo((Class) SimplePersonRepository.class));
        assertThat(metadata.getEntityType(), equalTo((Class) Person.class));
        assertThat(metadata.getIdentifierType(), equalTo((Class) String.class));
        assertThat(metadata.getIdentifierProperty(), is("id"));
    }

}