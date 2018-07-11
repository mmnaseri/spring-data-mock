package com.mmnaseri.utils.spring.data.sample.repositories;

import com.mmnaseri.utils.spring.data.sample.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Balthasar Biedermann
 * @since 1.1.5 (11/7/18)
 */
public interface JpaPersonRepository extends JpaRepository<Person, String> {
}
