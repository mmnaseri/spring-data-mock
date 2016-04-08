package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import com.mmnaseri.utils.spring.data.error.RepositoryDefinitionException;
import org.springframework.data.repository.RepositoryDefinition;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class AnnotationRepositoryMetadataResolverTest {

    public interface SampleUnannotatedRepository {}

    @RepositoryDefinition(domainClass = Person.class, idClass = String.class)
    public interface SampleAnnotatedRepository {

    }

    @Test(expectedExceptions = RepositoryDefinitionException.class, expectedExceptionsMessageRegExp = ".*?: Expected the repository to be annotated with @RepositoryDefinition")
    public void testResolvingFromRepositoryWithoutAnnotations() throws Exception {
        new AnnotationRepositoryMetadataResolver().resolve(SampleUnannotatedRepository.class);
    }

    @Test
    public void testResolvingFromAnnotatedRepository() throws Exception {
        final RepositoryMetadata metadata = new AnnotationRepositoryMetadataResolver().resolve(SampleAnnotatedRepository.class);
        assertThat(metadata, is(notNullValue()));
        assertThat(metadata.getRepositoryInterface(), equalTo((Class) SampleAnnotatedRepository.class));
        assertThat(metadata.getEntityType(), equalTo((Class) Person.class));
        assertThat(metadata.getIdentifierType(), equalTo((Class) String.class));
        assertThat(metadata.getIdentifierProperty(), is("id"));
    }

}