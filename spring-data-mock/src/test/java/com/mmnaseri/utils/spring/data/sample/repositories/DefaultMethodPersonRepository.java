package com.mmnaseri.utils.spring.data.sample.repositories;

import com.mmnaseri.utils.spring.data.sample.models.Person;

import java.util.List;

public interface DefaultMethodPersonRepository extends SimplePersonRepository {

  List<Person> findByAgeGreaterThan(int age);

  default List<Person> theInterestingPeople() {
    return findByAgeGreaterThan(99);
  }

}
