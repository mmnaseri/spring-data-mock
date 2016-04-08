package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import com.mmnaseri.utils.spring.data.error.RepositoryDefinitionException;
import org.springframework.data.repository.Repository;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class AssignableRepositoryMetadataResolverTest {

    public interface SampleNonInheritingRepository {}

    public interface SampleInheritingRepository extends Repository<Person, String> {}

    @Test(expectedExceptions = RepositoryDefinitionException.class, expectedExceptionsMessageRegExp = ".*?: Expected interface to extend .*?\\.Repository")
    public void testResolvingFromNonInheritingRepository() throws Exception {
        new AssignableRepositoryMetadataResolver().resolve(SampleNonInheritingRepository.class);
    }

    @Test
    public void testResolvingFromInheritingRepository() throws Exception {
        final RepositoryMetadata metadata = new AssignableRepositoryMetadataResolver().resolve(SampleInheritingRepository.class);
        assertThat(metadata, is(notNullValue()));
        assertThat(metadata.getRepositoryInterface(), equalTo((Class) SampleInheritingRepository.class));
        assertThat(metadata.getEntityType(), equalTo((Class) Person.class));
        assertThat(metadata.getIdentifierType(), equalTo((Class) String.class));
        assertThat(metadata.getIdentifierProperty(), is("id"));
    }

}