package com.mmnaseri.utils.spring.data.sample.repositories;

import com.mmnaseri.utils.spring.data.sample.models.Person;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 7:04 PM)
 */
@RepositoryDefinition(domainClass = Person.class, idClass = String.class)
public interface SampleAnnotatedRepository {

}
