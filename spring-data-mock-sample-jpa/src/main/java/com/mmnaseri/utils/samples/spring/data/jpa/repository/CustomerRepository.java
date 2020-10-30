package com.mmnaseri.utils.samples.spring.data.jpa.repository;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Customer;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/12/16, 1:53 PM)
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  List<Customer> findByBirthdayBetween(Date from, Date to);

  @SuppressWarnings("SpringDataMethodInconsistencyInspection") // This is covered in a custom impl.
  List<Customer> findByExample(Example<Customer> probe);

  List<Customer> findByFirstNameIn(Collection<String> firstNames);

  List<Customer> findByFirstNameIgnoreCaseContaining(String substring);
}
