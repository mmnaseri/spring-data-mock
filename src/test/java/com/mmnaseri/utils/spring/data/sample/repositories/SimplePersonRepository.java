package com.mmnaseri.utils.spring.data.sample.repositories;

import com.mmnaseri.utils.spring.data.sample.models.Person;
import org.springframework.data.repository.Repository;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public interface SimplePersonRepository extends Repository<Person, String> {
}
