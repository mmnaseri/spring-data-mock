package com.mmnaseri.utils.spring.data.sample.repositories;

import com.mmnaseri.utils.spring.data.sample.models.Address;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 7:05 PM)
 */
@RepositoryDefinition(domainClass = Person.class, idClass = String.class)
public interface AnnotatedInheritingRepository extends Repository<Address, String> {
}
