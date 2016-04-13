package com.mmnaseri.utils.spring.data.sample.repositories;

import com.mmnaseri.utils.spring.data.sample.models.Person;
import org.springframework.data.repository.Repository;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public interface SimplePersonRepository extends Repository<Person, String> {
}
