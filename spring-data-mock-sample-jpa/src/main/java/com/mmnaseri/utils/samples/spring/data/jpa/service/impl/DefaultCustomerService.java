package com.mmnaseri.utils.samples.spring.data.jpa.service.impl;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Customer;
import com.mmnaseri.utils.samples.spring.data.jpa.repository.CustomerRepository;
import com.mmnaseri.utils.samples.spring.data.jpa.service.CustomerService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.Date;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/12/16, 1:55 PM)
 */
public class DefaultCustomerService implements CustomerService {

    private final CustomerRepository repository;

    public DefaultCustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public long register(String firstName, String lastName, Date birthday) {
        final Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setBirthday(birthday);
        return repository.save(customer).getId();
    }

    public Customer findCustomer(long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Customer> findCustomersByBirthday(Date from, Date to) {
        return repository.findByBirthdayBetween(from, to);
    }

    public List<Customer> findCustomersByName(String firstName, String lastName) {
        final Customer probe = new Customer();
        probe.setFirstName(firstName);
        probe.setLastName(lastName);
        final ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("firstName", ignoreCase())
                .withMatcher("lastName", ignoreCase());
        final Example<Customer> example = Example.of(probe, matcher);
        return repository.findByExample(example);
    }

}
