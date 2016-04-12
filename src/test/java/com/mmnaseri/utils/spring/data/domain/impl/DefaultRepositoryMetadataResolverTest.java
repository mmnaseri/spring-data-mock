package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.model.Address;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.RepositoryDefinition;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class DefaultRepositoryMetadataResolverTest {

    @Test
    public void testThatItResolvesUsingAnnotations() throws Exception {
        final RepositoryMetadata metadata = new DefaultRepositoryMetadataResolver().resolveFromInterface(AnnotatedInterface.class);
        assertThat(metadata, is(notNullValue()));
    }

    @Test
    public void testThatItResolvesUsingInheritance() throws Exception {
        final RepositoryMetadata metadata = new DefaultRepositoryMetadataResolver().resolveFromInterface(InheritingInterface.class);
        assertThat(metadata, is(notNullValue()));
    }

    @Test
    public void testThatAnnotationTakesPrecedenceOverInheritance() throws Exception {
        final RepositoryMetadata metadata = new DefaultRepositoryMetadataResolver().resolveFromInterface(AnnotatedInheritingInterface.class);
        assertThat(metadata, is(notNullValue()));
        assertThat(metadata.getEntityType(), is(equalTo(Person.class)));
    }

    @RepositoryDefinition(domainClass = Person.class, idClass = String.class)
    public interface AnnotatedInterface {}

    public interface InheritingInterface extends Repository<Person, String> {}

    @RepositoryDefinition(domainClass = Person.class, idClass = String.class)
    public interface AnnotatedInheritingInterface extends Repository<Address, String> {}

}