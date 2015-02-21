package com.agileapes.utils.spring.impl.repository;

import com.agileapes.utils.spring.impl.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/22 AD, 18:30)
 */
public interface PersonRepository extends MongoRepository<Person, String> {

    List<Person> findDistinctTop10PeopleByLastNameAndFirstNameLikeOrFirstNameStartsWithOrderByIdDesc(String lastName, String firstName, String alternateFirstName);

    long countByLastNameLike(String lastName);

}
